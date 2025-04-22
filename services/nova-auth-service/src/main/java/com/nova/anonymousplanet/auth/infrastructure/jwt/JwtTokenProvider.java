package com.nova.anonymousplanet.auth.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.infrastructure.jwt
 * fileName : JwtTokenProvider
 * author : Jinhong Min
 * date : 2025-04-21
 * description : JWT관련 유틸리티 클래스
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-21         Jinhong Min         최초 생성
 * ==============================================
 */

@Slf4j
@Component
public class JwtTokenProvider {

    private final long accessTokenExpireMillis;
    private final long refreshTokenExpireMillis;
    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;

    public JwtTokenProvider(
            @Value("${jwt.access-token.secret}") String accessSecretKey,
            @Value("${jwt.refresh-token.secret}") String refreshSecretKey,
            @Value("${jwt.access-token.expiration}") long accessTokenExpireMillis,
            @Value("${jwt.refresh-token.expiration}") long refreshTokenExpireMillis
    ) {
        this.accessTokenExpireMillis = accessTokenExpireMillis;
        this.refreshTokenExpireMillis = refreshTokenExpireMillis;
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * Access Token 생성
     */
    public String generateAccessToken(String uuid, String role, String roleGroup, String deviceId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpireMillis * 1000);

        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("role", role)
                .claim("roleGroup", roleGroup)
                .claim("deviceId", deviceId)
                .signWith(accessSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(String uuid, String deviceId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpireMillis * 1000);

        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("deviceId", deviceId)
                .signWith(refreshSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Access Token 검증
     */
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecretKey);
    }

    /**
     * Refresh Token 검증
     */
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecretKey);
    }

    private boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Access Token의 Claims 추출
     */
    public Claims getAccessTokenClaims(String token) {
        return parseClaims(token, accessSecretKey);
    }

    /**
     * Refresh Token의 Claims 추출
     */
    public Claims getRefreshTokenClaims(String token) {
        return parseClaims(token, refreshSecretKey);
    }

    private Claims parseClaims(String token, SecretKey key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
