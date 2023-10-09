package com.example.securityservice.login.domain.dto;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String username;
    private Integer status;
    private String authorities;
}
