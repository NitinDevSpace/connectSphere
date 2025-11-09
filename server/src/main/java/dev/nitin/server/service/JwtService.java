package dev.nitin.server.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).get("name", String.class);
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
           // System.out.println(">>> Claims: " + extractAllClaims(token));
            //System.out.println(">>> Token is valid");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}