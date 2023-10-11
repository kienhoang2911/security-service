package com.example.securityservice.login.controller;


import com.example.securityservice.config.JwtTokenUtil;
import com.example.securityservice.login.domain.dto.*;
import com.example.securityservice.login.service.AuthenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenController {

    private final AuthenService authenService;

    private final JwtTokenUtil jwtTokenUtil;

    private static final String authorizationRequestBaseUri = "oauth2/authorize-client";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();


    private final ClientRegistrationRepository clientRegistrationRepository;


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

    @GetMapping("/oauth_login")
    public String getLoginPage(Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }



        clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2AuthenticationUrls);



        return "templates/oauth_login.html";
    }
}
