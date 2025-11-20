package com.backend.dashboarddemo.jwt;

import com.backend.dashboarddemo.model.User;
import com.backend.dashboarddemo.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${spring.app.jwt.secret}")
    private String secret;
    @Value("${spring.app.jwt.access-expiration-ms}")
    private long accessExpirationMs;
    @Value("${spring.app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpirationMs);

        var rolesCopy = user.getRoles() == null
                ? java.util.List.<UserRole>of()
                : new java.util.ArrayList<>(user.getRoles());

        String roles = rolesCopy.stream()
                .map(UserRole::getName)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("uid", user.getId())
                .claim("roles", roles)
                .claim("type", "ACCESS")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }


    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshExpirationMs);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("uid", user.getId())
                .claim("type", "REFRESH")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
        Object type = claims.get("type");
        return "ACCESS".equals(type);
    }

    public boolean isRefreshToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
        Object type = claims.get("type");
        return "REFRESH".equals(type);
    }
}
