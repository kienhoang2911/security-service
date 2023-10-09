package com.example.securityservice.login.domain.model;


import com.example.securityservice.login.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BasicLogin {


    private String id;


    private String username;

    private String password;

    private Integer status;

    private String authorities;

    public UserDto toDto() {
        UserDto dto = new UserDto();
        dto.setId(this.getId());
        dto.setUsername(this.getUsername());
        dto.setStatus(this.getStatus());
        dto.setAuthorities(this.authorities);
        return dto;
    }
}
