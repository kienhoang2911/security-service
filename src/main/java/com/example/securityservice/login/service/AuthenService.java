package com.example.securityservice.login.service;


import com.example.securityservice.login.domain.dto.JwtRequest;
import com.example.securityservice.login.domain.dto.JwtResponse;
import com.example.securityservice.login.domain.dto.RefreshTokenPayloadDto;
import com.example.securityservice.login.domain.dto.UserDto;

public interface AuthenService {

    UserDto login(JwtRequest request);

//    UserDto register(JwtRequest request);
//
    JwtResponse refreshToken(RefreshTokenPayloadDto payload);
}
