package com.example.securityservice.login.service.impl;


import com.example.securityservice.config.JwtTokenUtil;
import com.example.securityservice.login.domain.dto.JwtRequest;
import com.example.securityservice.login.domain.dto.JwtResponse;
import com.example.securityservice.login.domain.dto.RefreshTokenPayloadDto;
import com.example.securityservice.login.domain.dto.UserDto;
import com.example.securityservice.login.domain.model.BasicLogin;
import com.example.securityservice.login.service.AuthenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenService {



    private final PasswordEncoder bcryptEncoder;

    private final JwtTokenUtil jwtTokenUtil;



    @Override
    public UserDto login(JwtRequest request) {
        BasicLogin basicLogin = new BasicLogin();
        basicLogin.setId("1");
        basicLogin.setUsername("kien");
        basicLogin.setPassword(bcryptEncoder.encode("123456a@"));
        if (ObjectUtils.isEmpty(basicLogin)){
            throw new SecurityException("USER_NOT_FOUND");
        }
        UserDto userDto = new UserDto();
        if (bcryptEncoder.matches(request.getPassword(), basicLogin.getPassword())) {
            userDto.setId(basicLogin.getId());
            userDto.setUsername(basicLogin.getUsername());
        } else {
            throw new SecurityException("UNAUTHORIZE");
        }
        return userDto;
    }

//    @Override
//    public UserDto register(JwtRequest request) {
//        //Check trùng username
////        var existUser = userRepository.findByUsernameAndStatus(request.getUsername(),1);
////        Assert.isNull(existUser,"User đã tồn tại");
//        // Lưu tài khoản vừa tạo
//        BasicLogin basicLogin = new BasicLogin();
//        basicLogin.setId(UUID.randomUUID().toString());
//        basicLogin.setUsername(request.getUsername());
//        basicLogin.setPassword(bcryptEncoder.encode(request.getPassword()));
//        basicLogin.setStatus(1);
////        userRepository.save(basicLogin);
////        return basicLogin.toDto();
//        return null;
//    }
//
    @Override
    public JwtResponse refreshToken(RefreshTokenPayloadDto payload) {
        //Check username
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(payload.getRefreshToken());
        String username = claims.getSubject();
        var existUser = "";
        if (ObjectUtils.isEmpty(existUser)){
            throw new SecurityException("USER_NOT_FOUND");
        }
        if (!"refreshToken".equals(claims.getAudience())) {
            throw new SecurityException("REFRESH_TOKEN_INVALID");
        }
        JwtResponse jwtResponse = jwtTokenUtil.generateToken("",username);
        return jwtResponse;
    }
}
