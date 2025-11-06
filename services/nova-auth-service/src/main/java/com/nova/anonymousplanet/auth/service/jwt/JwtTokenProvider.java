package com.nova.anonymousplanet.auth.service.jwt;

import com.nova.anonymousplanet.auth.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
     * userUuid와 Role, deviceId를 저장
     */
    public String createAccessToken(TokenDto.IssueRequest reqDto) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpireMillis * 1000);

        return Jwts.builder()
                .subject(reqDto.userUuid())
                .issuedAt(now)
                .expiration(expiry)
                .claim("role", reqDto.role().getCode())
                .claim("deviceId", reqDto.deviceId())
                .signWith(accessSecretKey, Jwts.SIG.HS256)
                .compact()
                ;
    }

    /**
     * Refresh Token을 생성합니다.
     * userUuid와 deviceId를 저장
     */
    public String createRefreshToken(TokenDto.IssueRequest reqDto) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpireMillis * 1000);

        return Jwts.builder()
                .subject(reqDto.userUuid())
                .issuedAt(now)
                .expiration(expiry)
                .claim("deviceId", reqDto.deviceId())
                .signWith(accessSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * AccessToken의 유효성을 검증합니다.
     */
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecretKey);
    }

    /**
     * RefreshToken의 유효성을 검증합니다.
     */
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecretKey);
    }

    /**
     * JWT 토큰의 서명과 만료 여부를 검증합니다.
     * @param token
     * @param key
     * @return
     */
    private boolean validateToken(String token, SecretKey key) {
       try {
           Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
           return true;
       } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
           log.error("code: C001-001, message: 잘못된 JWT 서명입니다.");
       } catch (ExpiredJwtException e) {
           log.error("code: C001-002, message: 만료된 JWT 토큰입니다.");
       } catch (UnsupportedJwtException e) {
           log.error("code: C001-003, message: 지원되지 않는 JWT 토큰입니다.");
       } catch (IllegalArgumentException e) {
           log.error("code: C001-004, message: JWT 토큰이 잘못되었습니다.");
       }
       return false;
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
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to parse {} claims. token={}, error={}", tokenType, token, e.getMessage());
            throw new RuntimeException(tokenType + " 파싱 실패", e);
        }
    }
}
