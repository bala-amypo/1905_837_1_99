package com.example.demo.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    private final Key key;
    private final long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiration = expiration;
    }

    // ✅ SINGLE STANDARD TOKEN FORMAT (tests + auth use same)
    public String generateToken(String email, Long userId, Set<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("userId", userId);
        claims.put("roles", new ArrayList<>(roles));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Map<String, Object> getClaims(String token) {
        Claims claims = parseClaims(token);
        Map<String, Object> map = new HashMap<>();
        map.put("email", claims.get("email"));
        map.put("userId", claims.get("userId"));
        map.put("roles", claims.get("roles"));
        return map;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }
}
