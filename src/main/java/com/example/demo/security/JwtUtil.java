package com.example.demo.security;

import com.example.demo.entity.UserAccount;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;

import java.util.Date;

public class JwtUtil {

    private final String secret;
    private final long validityInMs;

    public JwtUtil(String secret, long validityInMs) {
        this.secret = secret;
        this.validityInMs = validityInMs;
    }

    public String generateToken(Authentication authentication, UserAccount user) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}



// package com.example.demo.security;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.stereotype.Component;

// import javax.crypto.SecretKey;
// import java.util.Date;

// @Component
// public class JwtUtil {

//     private static  String secret = "ZmRnaGprbG1hYm5vcHFyc3R1dnd4eXo0";
//     private static long validityInMs = 3600000;

//     // Public constructor
//     public JwtUtil() {
//         this.secret = "ZmRnaGprbG1hYm5vcHFyc3R1dnd4eXo0"; // default secret
//         this.validityInMs = 3600000; // default 1 hour
//     }

//     // Optional: Constructor with parameters
//     public JwtUtil(String secret, long validityInMs) {
//         this.secret = secret;
//         this.validityInMs = validityInMs;
//     }
//     private final SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());
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
//                 .signWith(SignatureAlgorithm.HS256, key)
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


