package com.nova.anonymousplanet.gateway.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String ROLE_KEY = "role";

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, String> accessTokenValidation(String accessToken) {
        Map<String, String> errorMap = new HashMap<>();
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
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

    public String getUUIdFromToken(String token){
        return this.getClaims(token).getSubject();
    }

    public String getRoleFormToken(String token) {
       return String.valueOf(this.getClaims(token).get(ROLE_KEY));
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch(ExpiredJwtException e) {
           return e.getClaims();
        }
    }
}
