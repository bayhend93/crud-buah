package com.example.spring.boot.crud.spring_boot;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtil {
    public static final String SECRET_KEY = "b64d2e3a80f86c6a3f7df9f5415d9f6e2d3b4b57cda0de88b7c80fa4fc3f84e5";
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2)) // 2 menit expiry
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

}
