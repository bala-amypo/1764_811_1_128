package com.example.demo.config;

import com.example.demo.entity.UserAccount;
import com.example.demo.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;

    public JwtTokenProvider(String secret, long validityInMs) {
        this.jwtUtil = new JwtUtil(secret, validityInMs);
    }

    public JwtTokenProvider() {
        this.jwtUtil = new JwtUtil();
    }

    public String generateToken(Authentication authentication, UserAccount user) {
        
        return jwtUtil.generateToken(authentication.getName());
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }
}
