package com.nova.anonymousplanet.auth.infrastructure.jwt;

import com.nova.anonymousplanet.auth.domain.jwt.dto.RefreshTokenStoreDto;
import com.nova.anonymousplanet.common.constant.RoleCode;
import com.nova.anonymousplanet.common.constant.RoleGroupCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.infrastructure.jwt
 * fileName : JwtRefreshTokenStore
 * author : Jinhong Min
 * date : 2025-04-22
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-22     Jinhong Min     최초 생성
 * ==============================================
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRefreshTokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    public void store(RefreshTokenStoreDto.StoreRequest reqDto) {
        String key = reqDto.getKey();
        try {
            Map<String, String> valueMap = Map.of(
                    "refreshToken", reqDto.getRefreshToken(),
                    "roleGroup", reqDto.getRoleGroup().getCode(),
                    "role", reqDto.getRole().getCode(),
                    "userId", reqDto.getUserId().toString()
            );
            redisTemplate.opsForHash().putAll(key, valueMap);
            redisTemplate.expire(key, reqDto.getExpirationSeconds(), TimeUnit.SECONDS);
            log.debug("Stored RefreshToken in Redis. key={}, expiration={}s", key, reqDto.getExpirationSeconds());
        } catch(Exception e) {
            log.error("Failed to store RefreshToken in Redis. key={}", key, e);
            throw new RuntimeException("Redis 저장 실패", e);
        }
    }

    public Optional<RefreshTokenStoreDto.GetResponse> get(RefreshTokenStoreDto.GetRequest reqDto) {
        String key = reqDto.getKey();
        try {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

            if (entries.isEmpty()) {
                return Optional.empty();
            }

            String userIdStr = (String) entries.get("userId");
            if (userIdStr == null) {
                throw new IllegalStateException("Redis에 userId가 없습니다. key=" + key);
            }

            return Optional.of(
                    new RefreshTokenStoreDto.GetResponse(
                            (String) entries.get("refreshToken"),
                            RoleGroupCode.fromCode((String) entries.get("roleGroup")),
                            RoleCode.fromCode((String) entries.get("role")),
                            Long.parseLong(userIdStr)
                    )
            );
        } catch (Exception e) {
            log.error("Failed to get RefreshToken from Redis. key={}", key, e);
            throw new RuntimeException("Redis 조회 실패", e);
        }
    }

    public boolean validate(RefreshTokenStoreDto.ValidateRequest reqDto) {
        String key = reqDto.getKey();
        try {
            Object storedToken = redisTemplate.opsForHash().get(key, "refreshToken");
            return Objects.equals(reqDto.getRefreshToken(), storedToken);
        } catch (Exception e) {
            log.error("Failed to validate RefreshToken in Redis. key={}", key, e);
            throw new RuntimeException("Redis 검증 실패", e);
        }
    }

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
