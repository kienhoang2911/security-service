package com.example.securityservice.login.service;


import com.example.securityservice.login.domain.model.BasicLogin;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BasicLogin basicLogin = new BasicLogin();
        basicLogin.setId("1");
        basicLogin.setUsername("kien");
        basicLogin.setAuthorities("L");
        basicLogin.setPassword("123456a@");
        if (basicLogin != null) {
            String authorities = basicLogin.getAuthorities();
            List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(authorities));
            return new org.springframework.security.core.userdetails.User(username, basicLogin.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}
