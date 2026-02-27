package com.vasyerp.jwtauthentication.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private final String JWT_SECRET = "mySecretKeyForJWTTokenGenerationAndValidationPurposeOnly12345" ;

    private final long JWT_EXPIRATION_MS = 900000;

    public String generateToken(String username, String role) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + JWT_EXPIRATION_MS);

        try {
            return Jwts.builder()
                    .subject(username)
                    .claim("username", username)
                    .claim("role", role)
                    .claim("issuedAt", issuedAt.getTime())
                    .claim("expiration", expiration.getTime())
                    .issuedAt(issuedAt)
                    .expiration(expiration)
                    .signWith(getSigningKey())
                    .compact();
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = extractClaims(token);
            String username = claims.get("username", String.class);
            return username != null ? username : claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getRoleFromToken(String token) {
        try {
            return extractClaims(token).get("role", String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long getExpirationTimeFromToken(String token) {
        try {
            return extractClaims(token).getExpiration().getTime();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    private SecretKey getSigningKey() {
        try {
            return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        } catch (WeakKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
