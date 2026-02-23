package com.nova.anonymousplanet.gateway.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class JwtTokenProvider {

    private final long accessTokenExpireMillis;
    private final long refreshTokenExpireMillis;
    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;

    public JwtTokenProvider(
        @Value("${nova.jwt.access-token.secret}") String accessSecretKey,
        @Value("${nova.jwt.refresh-token.secret}") String refreshSecretKey,
        @Value("${nova.jwt.access-token.expiration}") long accessTokenExpireMillis,
        @Value("${nova.jwt.refresh-token.expiration}") long refreshTokenExpireMillis
    ) {
        this.accessTokenExpireMillis = accessTokenExpireMillis;
        this.refreshTokenExpireMillis = refreshTokenExpireMillis;
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * AccessToken의 유효성을 검증합니다.
     */
    public Map<String, String> validateAccessToken(String token) {
        return validateToken(token, accessSecretKey);
    }

    /**
     * RefreshToken의 유효성을 검증합니다.
     */
    public Map<String, String> validateRefreshToken(String token) {
        return validateToken(token, refreshSecretKey);
    }

    private Map<String, String> validateToken(String token, SecretKey key) {
        Map<String, String> errorMap = new HashMap<>();
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return null;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
            errorMap.put("code", "C001-001");
            errorMap.put("error", e.getClass().getSimpleName());
            errorMap.put("message", "잘못된 JWT 서명입니다.");
            errorMap.put("detailMessage", "잘못된 JWT 서명입니다. 다시 로그인 해주세요.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            errorMap.put("code", "C001-002");
            errorMap.put("error", e.getClass().getSimpleName());
            errorMap.put("message", "토큰이 만료 되었습니다.");
            errorMap.put("detailMessage", "만료된 JWT 토큰입니다. 다시 로그인 해주세요");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            errorMap.put("code", "C001-003");
            errorMap.put("error", e.getClass().getSimpleName());
            errorMap.put("message", "지원되지 않는 JWT 토큰입니다..");
            errorMap.put("detailMessage", "지원 되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            errorMap.put("code", "C001-004");
            errorMap.put("error", e.getClass().getSimpleName());
            errorMap.put("message", "JWT 토큰이 잘못 되었습니다. ");
            errorMap.put("detailMessage", "해당 토큰은 잘못된 JWT 서명입니다.");
        }
        return errorMap;
    }

    public String getUserUuid(String token){
        Claims claims = parseClaims(token, accessSecretKey);
        return Objects.requireNonNull(claims).getSubject();
    }

    public String getRole(String token) {
        Claims claims = parseClaims(token, accessSecretKey);
        return Objects.requireNonNull(claims).get("role", String.class);
    }

    public String getDeviceId(String token) {
        Claims claims = parseClaims(token, accessSecretKey);
        return Objects.requireNonNull(claims).get("deviceId", String.class);
    }



    /**
     * JWT 토큰에서 Claims를 파싱하여 반환합니다.
     */
    private Claims parseClaims(String token, SecretKey key) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to parse");
            return null;
        }
    }
}
