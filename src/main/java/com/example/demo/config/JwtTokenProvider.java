package com.example.demo.config;

import com.example.demo.entity.UserAccount;
import com.example.demo.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;

    public JwtTokenProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String generateToken(Authentication authentication, UserAccount user) {
        // Use authentication if needed, here just username
        return jwtUtil.generateToken(authentication);
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }
}
