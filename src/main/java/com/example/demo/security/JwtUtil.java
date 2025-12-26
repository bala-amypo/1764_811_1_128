// package com.example.demo.security;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import org.springframework.stereotype.Component;

// import java.util.Date;

// @Component
// public class JwtUtil {

//     private final String secret;
//     private final long validityInMs;

//     // Public constructor
//     public JwtUtil() {
//         this.secret = "asdfghjkl"; // default secret
//         this.validityInMs = 3600000; // default 1 hour
//     }

//     // Optional: Constructor with parameters
//     public JwtUtil(String secret, long validityInMs) {
//         this.secret = secret;
//         this.validityInMs = validityInMs;
//     }

//     public String generateToken(Long userId, String email, String role) {

//         Claims claims = Jwts.claims();
//         claims.put("userId", userId);
//         claims.put("email", email);
//         claims.put("role", role);

//         Date now = new Date();
//         Date expiry = new Date(now.getTime() + validityInMs);

//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setIssuedAt(now)
//                 .setExpiration(expiry)
//                 .signWith(SignatureAlgorithm.HS256, secret)
//                 .compact();
//     }

//     public boolean validateToken(String token) {
//         try {
//             Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//             return true;
//         } catch (Exception ex) {
//             return false;
//         }
//     }

//     public Claims getClaims(String token) {
//         return Jwts.parser()
//                 .setSigningKey(secret)
//                 .parseClaimsJws(token)
//                 .getBody();
//     }

//     public String getEmail(String token) {
//         return getClaims(token).get("email", String.class);
//     }
// }


package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    private final String secretKey;
    private final long expirationMillis;
    private final Key key;
    
    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.expiration}") long expirationMillis) {
        this.secretKey = secretKey;
        this.expirationMillis = expirationMillis;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    
    public String generateToken(Long customerId, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("role", role);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException e) {
            throw e;
        }
    }
    
    public String extractEmail(String token) {
        return validateToken(token).get("email", String.class);
    }
    
    public Long extractCustomerId(String token) {
        Claims claims = validateToken(token);
        Object customerIdObj = claims.get("customerId");
        if (customerIdObj instanceof Integer) {
            return ((Integer) customerIdObj).longValue();
        }
        return (Long) customerIdObj;
    }
    
    public String extractRole(String token) {
        return validateToken(token).get("role", String.class);
    }
}