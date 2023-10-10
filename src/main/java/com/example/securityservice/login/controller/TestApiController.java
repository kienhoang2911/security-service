package com.example.securityservice.login.controller;

import com.example.securityservice.login.domain.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")

public class TestApiController {
    @PostMapping(value = "/test1")
    public ResponseEntity<ResponseDto> login() {
        try {
            String test = "Call test thành công";
            return ResponseEntity.ok(ResponseDto.success("SUCCESS", test));
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseDto.error("500", e.getMessage()));
        }
    }
//
//    @PostMapping(value = "/admin")
//    public ResponseEntity<ResponseDto> testAdmin() {
//        try {
//            String test = "Call admin thành công";
//            return ResponseEntity.ok(ResponseDto.success("SUCCESS", test));
//        } catch (Exception e) {
//            return ResponseEntity.ok(ResponseDto.error("500", e.getMessage()));
//        }
//    }
//
//    @PostMapping(value = "/user")
//    public ResponseEntity<ResponseDto> testUser() {
//        try {
//            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            String test = "Call user thành công";
//            return ResponseEntity.ok(ResponseDto.success("SUCCESS", test));
//        } catch (Exception e) {
//            return ResponseEntity.ok(ResponseDto.error("500", e.getMessage()));
//        }
//    }

}
