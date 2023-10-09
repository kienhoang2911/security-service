package com.example.securityservice.login.controller;


import com.example.securityservice.config.JwtTokenUtil;
import com.example.securityservice.login.domain.dto.*;
import com.example.securityservice.login.service.AuthenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenController {

    private final AuthenService authenService;

    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseDto> login(@RequestBody JwtRequest request) {
        try {
            UserDto userDto = authenService.login(request);
            JwtResponse jwtResponse = jwtTokenUtil.generateToken(userDto.getId(),userDto.getUsername());
            return ResponseEntity.ok(ResponseDto.success("SUCCESS", jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseDto.error("500", e.getMessage()));
        }
    }

//    @PostMapping(value = "/register")
//    private ResponseEntity<ResponseDto> register(@RequestBody JwtRequest request) {
//        try {
//            UserDto userDto = authenService.register(request);
//            return ResponseEntity.ok(ResponseDto.success("SUCCESS",userDto));
//        } catch (Exception e) {
//            return ResponseEntity.ok(ResponseDto.error("500", e.getMessage()));
//        }
//    }
//
    @PostMapping(value = "/refresh-token")
    public ResponseEntity<ResponseDto> refreshToken(@RequestBody RefreshTokenPayloadDto payload) {
        try {
            return ResponseEntity.ok(ResponseDto.success("SUCCESS", authenService.refreshToken(payload)));
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseDto.error("500", e.getMessage()));
        }
    }
}
