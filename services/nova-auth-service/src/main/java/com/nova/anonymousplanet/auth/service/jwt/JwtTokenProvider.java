package com.nova.anonymousplanet.auth.service.jwt;

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
 * description : JWT 토큰(Access/Refresh)의 생성, 검증 및 클레임 추출 기능을 담당하는 유틸리티 클래스.
 *               인증 서버에서 JWT 기반 인증 로직 구현 시 핵심적인 역할을 하며, 보안 키를 기반으로 서명된 토큰을 발급하고
 *               클라이언트로부터 전달받은 토큰의 유효성을 확인하고 필요한 정보를 추출하는 역할을 수행함.
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
     * Access Token을 생성합니다.
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
     * Refresh Token을 생성합니다.
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
     * Access Token의 유효성을 검증합니다.
     */
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecretKey);
    }

    /**
     * Refresh Token의 유효성을 검증합니다.
     */
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecretKey);
    }

    /**
     * JWT 토큰의 서명과 만료 여부를 검증합니다.
     */
    private boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Access Token에서 Claims 정보를 추출합니다.
     */
    public Claims getAccessTokenClaims(String token) {
        return parseClaims(token, accessSecretKey, "accessToken");
    }

    /**
     * Refresh Token에서 Claims 정보를 추출합니다.
     */
    public Claims getRefreshTokenClaims(String token) {
        return parseClaims(token, refreshSecretKey, "refreshToken");
    }

    /**
     * JWT 토큰에서 Claims를 파싱하여 반환합니다.
     */
    private Claims parseClaims(String token, SecretKey key, String tokenType) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to parse {} claims. token={}, error={}", tokenType, token, e.getMessage());
            throw new RuntimeException(tokenType + " 파싱 실패", e);
        }
    }
}
