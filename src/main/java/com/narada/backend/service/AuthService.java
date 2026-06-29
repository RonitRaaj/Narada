package com.narada.backend.service;

import com.narada.backend.dTO.sessiondTO.EnterSessionDTO;
import com.narada.backend.model.DeviceType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final long EXPIRATION_TIME = 86400000; 
    private final String SECRET_STRING = "your-ultra-secret-key-that-must-be-at-least-256-bits-long-narada-clipboard";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    public String authenticateSessionEntry(EnterSessionDTO request) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .claim("sessionId", request.getSessionId().toUpperCase())
                .claim("deviceType", request.getDeviceType().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();

        return token;
    }

    public EnterSessionDTO getSessionDataFromToken(String token) {

        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        token = token.trim();

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String sessionId = claims.get("sessionId", String.class);
        String deviceTypeStr = claims.get("deviceType", String.class);

        EnterSessionDTO sessionData = new EnterSessionDTO();
        sessionData.setSessionId(sessionId);
        sessionData.setDeviceType(DeviceType.valueOf(deviceTypeStr));

        return sessionData;
    }
}