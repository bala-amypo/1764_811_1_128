package com.example.demo.config;

import com.example.demo.entity.UserAccount;
import com.example.demo.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;

    // Constructor injection of JwtUtil
    public JwtTokenProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Generate JWT token for a given authenticated user.
     * @param authentication the authentication object (optional usage)
     * @param user the user account
     * @return JWT token as string
     */
    public String generateToken(Authentication authentication, UserAccount user) {
        // Forward to JwtUtil
        return jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
    }

    /**
     * Validate a JWT token.
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    /**
     * Extract username/email from JWT token.
     * @param token the JWT token
     * @return username/email
     */
    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }
}
