package com.example.securityservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig  {

    private static List<String> clients = Arrays.asList("google");
    private final JwtRequestFilter jwtRequestFilter;
    private final Environment env;
    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
    //JwtAuthenticationEntryPoint để xử lí thông tin từ request và gửi về response, chỗ này sẽ gửi về một lỗi khi mà request tới đường dẫn yêu cầu xác thực (cần chuỗi JWT) nhưng không có kèm chuỗi JWT
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    //JwtRequestFilter là lớp Filter tự tạo ra, có nhiệm vụ kiểm tra request của người dùng trước khi nó tới đích. Nó sẽ lấy Header Authorization ra và kiểm tra xem chuỗi JWT người dùng gửi lên có hợp lệ không.
//    private final JwtRequestFilter jwtRequestFilter;

//    @Bean
//    //HttpSecurity là đối tượng chính của Spring Security, cho phép chúng ta cấu hình mọi thứ cần bảo mật, và nó được xây dựng dưới design pattern giống với Builder Pattern, nên mọi cài đặt có thể viết liên tục thông qua toán tử .
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable() //CSRF ( Cross Site Request Forgery) là kĩ thuật tấn công bằng cách sử dụng quyền chứng thực của người sử dụng đối với 1 website khác
//        // Các trang không yêu cầu login như vậy ai cũng có thể vào được admin hay user hoặc guest có thể vào các trang
//        .authorizeRequests()
//                .antMatchers("/", "/auth/**", "/auth/**").permitAll()
////                .antMatchers("/test/admin").hasAuthority("ROLE_ADMIN")
////                .antMatchers("/test/user").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
//                .anyRequest().authenticated()
//                // exceptionHandling() là phương thức cho phép cấu hình xử lý ngoại lệ, exception được bắt đầu tại AuthenticationEntryPoint, chúng ta triển khai AuthenticationEntryPoint tùy chỉnh
//                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                //Khi sử dụng stateless session, session sẽ không được sử dụng để lưu trữ trạng thái của người dùng
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        //UsernamePasswordAuthenticationFilter là filter cố gắng tìm tham số request username/password hay POST body và nếu được tìm thấy, cố gắng authenticate user bằng các giá trị đó.
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                )
                .authorizeHttpRequests(a -> a.anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                ).sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).oauth2Login(Customizer.withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(c -> getRegistration(c))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }


    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
        if (client.equals("google")) {
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
//        if (client.equals("facebook")) {
//            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
//                    .clientId(clientId)
//                    .clientSecret(clientSecret)
//                    .build();
//        }
        return null;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
