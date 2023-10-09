package com.example.securityservice.config;


import com.example.securityservice.login.service.JwtUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Lấy beare token từ header
            String jwtToken = jwtTokenUtil.extractJwtFromRequest(request);
            if (StringUtils.hasText(jwtToken)) {
                // Check nếu là refresh token thì dừng
                if (jwtTokenUtil.isRefreshToken(jwtToken)) {
                    return;
                }
                // Lấy tên user từ token
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                // Tìm user trong DB
                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                // Check user trong DB và user get ra từ token
                if (jwtTokenUtil.validateToken(jwtToken,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Người dùng được xác thực, vượt qua Spring Security thành công
        filterChain.doFilter(request, response);
    }

}
