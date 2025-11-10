package com.nova.anonymousplanet.gateway.service.jwt;

import com.nova.anonymousplanet.gateway.dto.RefreshTokenStoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;


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
                        Long.parseLong((String) entries.get("userId")),
                        (String) entries.get("userStatus")
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
            return true;
        } catch (Exception e) {
            log.error("Failed to validate RefreshToken in Redis. -> key={}", reqDto.getKey());
            return false;
        }
    }
}