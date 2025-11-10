package com.nova.anonymousplanet.auth.service.jwt;

import com.nova.anonymousplanet.auth.dto.RefreshTokenStoreDto;
import com.nova.anonymousplanet.core.constant.RoleCode;
import com.nova.anonymousplanet.core.constant.UserStatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.infrastructure.jwt
 * fileName : JwtRefreshTokenStore
 * author : Jinhong Min
 * date : 2025-04-21
 * description : RefreshToken을 Redis에 저장, 조회, 검증, 삭제하는 기능을 제공하는 클래스.
 *               사용자 인증 상태 유지를 위한 RefreshToken 관리 역할을 하며,
 *               Redis를 통해 각 사용자의 토큰 및 관련 정보를 안전하게 처리함.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-21         Jinhong Min         최초 생성
 * ==============================================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRefreshTokenStore {
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * RefreshToken과 부가 정보를 Redis에 저장합니다.
     */
    public void store(RefreshTokenStoreDto.StoreRequest reqDto) {
        String key = reqDto.getKey();
        try {
            Map<String, String> valueMap = Map.of(
                "userId", reqDto.userId().toString(),
                "refreshToken", reqDto.refreshToken(),
                "role", reqDto.role().getCode(),
                "userStatus", reqDto.userStatus().getCode()

            );
            redisTemplate.opsForHash().putAll(key, valueMap);
            redisTemplate.expire(key, reqDto.expirationSeconds(), TimeUnit.SECONDS);
            log.debug("Stored RefreshToken in Redis. key={}, expiration={}s", key, reqDto.expirationSeconds());
        } catch(Exception e) {
            log.error("Failed to store RefreshToken in Redis. key={}", key, e);
            throw new RuntimeException("Redis 저장 실패", e);
        }
    }

    /**
     * Redis에서 저장된 RefreshToken 정보를 조회합니다.
     */
    public Optional<RefreshTokenStoreDto.GetResponse> get(RefreshTokenStoreDto.GetRequest reqDto) {
        String key = reqDto.getKey();
        try {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

            if (entries.isEmpty()) {
                return Optional.empty();
            }

            String userUuid = (String) entries.get("userUuid");
            if (userUuid == null) {
                throw new IllegalStateException("Redis에 userUuid가 없습니다. key=" + key);
            }

            return Optional.of(
                    new RefreshTokenStoreDto.GetResponse(
                        userUuid,
                        (String) entries.get("deviceId"),
                        Long.parseLong((String) entries.get("userId")),
                        (String) entries.get("refreshToken"),
                        RoleCode.fromCode((String) entries.get("role")),
                        UserStatusCode.fromCode((String)entries.get("userStatus"))
                    )
            );
        } catch (Exception e) {
            log.error("Failed to get RefreshToken from Redis. key={}", key, e);
            throw new RuntimeException("Redis 조회 실패", e);
        }
    }

    /**
     * 전달받은 RefreshToken이 Redis에 저장된 값과 일치하는지 검증합니다.
     */
    public boolean validate(RefreshTokenStoreDto.ValidateRequest reqDto) {
        try {
            Optional<RefreshTokenStoreDto.GetResponse> stored= get(new RefreshTokenStoreDto.GetRequest(reqDto.userUuid(), reqDto.deviceId()));
            if (stored.isEmpty()) {
                log.warn("[Redis] RefreshToken 존재하지 않음 -> userUuid={}, deviceId={}", reqDto.userUuid(), reqDto.deviceId());
                return false;
            }

            RefreshTokenStoreDto.GetResponse storedToken = stored.get();
            boolean valid = storedToken.refreshToken().equals(reqDto.refreshToken());
            if (!valid) log.warn("[Redis] RefreshToken 검증 실패 -> key={}", reqDto.getKey());
            return valid;

        } catch (Exception e) {
            log.error("Failed to validate RefreshToken in Redis. -> key={}", reqDto.getKey());
            throw new RuntimeException("Redis 검증 실패", e);
        }
    }

    /**
     * Redis에서 해당 key의 RefreshToken 정보를 삭제합니다.
     */
    public void delete(RefreshTokenStoreDto.DeleteRequest reqDto) {
        String key = reqDto.getKey();
        try {
            redisTemplate.delete(key);
            log.debug("Deleted RefreshToken from Redis. key={}", key);
        } catch (Exception e) {
            log.error("Failed to delete RefreshToken from Redis. key={}", key, e);
            throw new RuntimeException("Redis 삭제 실패", e);
        }
    }
}