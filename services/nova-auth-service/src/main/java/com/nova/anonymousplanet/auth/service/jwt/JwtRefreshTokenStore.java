package com.nova.anonymousplanet.auth.service.jwt;

import com.nova.anonymousplanet.auth.dto.v1.RefreshTokenStoreDto;
import com.nova.anonymousplanet.core.constant.RoleCode;
import com.nova.anonymousplanet.core.constant.UserStatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * RefreshToken과 부가 정보를 Redis에 저장합니다.
     * RefreshToken 정보를 Redis Hash 구조로 저장합니다.
     * Root Key: 'rt:{userUuid}' (사용자별 묶음)
     * Hash Field: deviceId (디바이스별 세션)
     */
    public void store(RefreshTokenStoreDto.StoreRequest reqDto) {
        String rootKey = reqDto.getRootKey();
        String hashField = reqDto.deviceId();
        try {

            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("userId", reqDto.userId().toString());
            sessionData.put("refreshToken", reqDto.refreshToken());
            sessionData.put("deviceId", reqDto.deviceId());
            sessionData.put("role", reqDto.role().getCode());
            sessionData.put("userStatus", reqDto.userStatus().getCode());
            // 1. Redis Hash에 세션 정보 저장 (HSET: Key, Field, Value)
            // Field(deviceId)를 Key가 아닌 Field로 사용하여 덮어쓰기 방지
            redisTemplate.opsForHash().put(rootKey, hashField, sessionData);

            // 2. 만료 시간 설정: Root Key에 만료 시간을 설정
            // 이 사용자의 모든 Refresh Token이 만료되는 시점 (가장 긴 유효 기간)
            redisTemplate.expire(rootKey, reqDto.expirationSeconds(), TimeUnit.SECONDS);
            log.debug("Stored RefreshToken in Redis. key={}, hashField={}, expiration={}s", rootKey, hashField, reqDto.expirationSeconds());
        } catch(Exception e) {
            log.error("Failed to store RefreshToken in Redis. key={}, hashField={}", rootKey, hashField, e);
            throw new RuntimeException("Redis 저장 실패", e);
        }
    }

    /**
     * Redis에서 저장된 RefreshToken 정보를 조회합니다.
     */
    public Optional<RefreshTokenStoreDto.GetResponse> get(RefreshTokenStoreDto.GetRequest reqDto) {
        String rootKey = reqDto.getRootKey();
        String hashField = reqDto.deviceId();
        try {
            // Redis Hash에서 특정 Field(deviceId)의 Value(Map<String, String>)를 가져옵니다.
             Object sessionData = redisTemplate.opsForHash().get(rootKey, hashField);

            if (sessionData == null) {
                // Root Key가 없거나 Hash Field가 없는 경우 (세션 없음)
                return Optional.empty();
            }

            // Map<String, String> 형태로 저장되었다고 가정하고 캐스팅
            Map<String, String> entries = (Map<String, String>) sessionData;

            // 💡 필수 데이터 유효성 검사 (세션 데이터가 파손된 경우)
            if (!entries.containsKey("refreshToken") || !entries.containsKey("userId")) {
                log.error("Corrupted RefreshToken data in Redis. rootKey={}, field={}", rootKey, hashField);
                // 손상된 데이터이므로 삭제를 고려하거나 예외 처리 필요
                return Optional.empty();
            }

            return Optional.of(
                    new RefreshTokenStoreDto.GetResponse(
                        reqDto.userUuid(),
                        entries.get("deviceId"),
                        Long.parseLong(entries.get("userId")),
                        entries.get("refreshToken"),
                        RoleCode.fromCode(entries.get("role")),
                        UserStatusCode.fromCode(entries.get("userStatus"))
                    )
            );
        } catch (Exception e) {
            log.error("Failed to get RefreshToken from Redis. key={}", rootKey, e);
            throw new RuntimeException("Redis 조회 실패", e);
        }
    }

    /**
     * Redis에서 rootKey에 해당하는 모든 RefreshToken 정보 (모든 디바이스)를 조회합니다.
     */
    public List<RefreshTokenStoreDto.GetResponse> getAll(RefreshTokenStoreDto.GetRequest reqDto) {
        String rootKey = reqDto.getRootKey();
        // 최종 반환할 DTO 리스트
        List<RefreshTokenStoreDto.GetResponse> responseList = new ArrayList<>();

        try {
            // 1. Redis Hash의 모든 Field와 Value를 한 번에 가져옵니다. (HGETALL)
            // 반환 타입: Map<HashKey, HashValue>
            // 여기서 HashKey = deviceId (String), HashValue = sessionData (Map<String, String>)
            // redisTemplate 설정에 따라 <Object, Object>로 받는 것이 안전할 수 있습니다.
            Map<Object, Object> allEntriesMap = redisTemplate.opsForHash().entries(rootKey);

            if (allEntriesMap == null || allEntriesMap.isEmpty()) {
                // Root Key가 아예 없는 경우 (빈 리스트 반환)
                return Collections.emptyList();
            }

            // 2. 가져온 Map의 각 Value(sessionData)를 DTO로 변환합니다.
            for (Object sessionDataObj : allEntriesMap.values()) {
                try {
                    // 'get' 메소드와 동일한 로직 시작
                    Map<String, String> entries = (Map<String, String>) sessionDataObj;

                    // 💡 필수 데이터 유효성 검사 (get과 동일)
                    if (!entries.containsKey("refreshToken") || !entries.containsKey("userId")) {
                        log.warn("Corrupted RefreshToken data in Redis (skipped). rootKey={}, data={}", rootKey, entries);
                        continue; // 이 항목은 건너뛰고 다음 항목 처리
                    }

                    // 'get' 메소드의 DTO 생성 로직과 동일
                    responseList.add(
                        new RefreshTokenStoreDto.GetResponse(
                            reqDto.userUuid(), // 요청 DTO에서 userUuid 사용
                            entries.get("deviceId"),
                            Long.parseLong(entries.get("userId")),
                            entries.get("refreshToken"),
                            RoleCode.fromCode(entries.get("role")),
                            UserStatusCode.fromCode(entries.get("userStatus"))
                        )
                    );
                    // 'get' 메소드와 동일한 로직 끝

                } catch (Exception e) {
                    // 개별 항목 변환 중 오류 발생 시, 로그만 남기고 계속 진행
                    log.error("Failed to parse one entry in getAll. rootKey={}, entry={}", rootKey, sessionDataObj, e);
                }
            }

            return responseList;

        } catch (Exception e) {
            log.error("Failed to get ALL RefreshTokens from Redis. key={}", rootKey, e);
            throw new RuntimeException("Redis HGETALL 조회 실패", e);
        }
    }

    /**
     * 전달받은 RefreshToken이 Redis에 저장된 값과 일치하는지 검증합니다.
     */
    public boolean validate(RefreshTokenStoreDto.ValidateRequest reqDto) {
        try {
            Optional<RefreshTokenStoreDto.GetResponse> stored= get(new RefreshTokenStoreDto.GetRequest(reqDto.userUuid(), reqDto.deviceId()));

            // userUuid를 key값으로 갖는 값이 있는지 확인
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

    public void delete(RefreshTokenStoreDto.DeleteRequest reqDto) {
        String rootKey = reqDto.getRootKey();

        try {
            // 1. Redis Hash에서 특정 필드(deviceId) 삭제 (HDEL: Key, Field)
            Long deletedCount = redisTemplate.opsForHash().delete(rootKey, reqDto.deviceId());

            if (deletedCount > 0) {
                log.debug("Deleted RefreshToken from Redis. key={}, hashField={}", rootKey, reqDto.deviceId());
            } else {
                // 키나 필드가 존재하지 않아도 성공으로 간주 (이미 삭제됨)
                log.warn("No RefreshToken found to delete (or already deleted). key={}, hashField={}", rootKey, reqDto.deviceId());
            }
        } catch (Exception e) {
            log.error("Failed to delete RefreshToken from Redis. key={}, hashField={}", rootKey, reqDto.deviceId(), e);
            throw new RuntimeException("Redis H-DELETE 실패", e);
        }
    }

    /**
     * Redis에서 해당 key의 RefreshToken 정보를 삭제합니다.
     */
    public void deleteAll(RefreshTokenStoreDto.DeleteRequest reqDto) {
        String rootKey = reqDto.getRootKey();

        try {
            // DEL 명령어로 Root Key 자체를 삭제하면 Hash 내부의 모든 Field(deviceId)가 함께 삭제됩니다.
            Boolean deleted = redisTemplate.delete(rootKey);

            if (Boolean.TRUE.equals(deleted)) {
                log.info("Successfully forced logout for user. userUuid={}", reqDto.userUuid());
            } else {
                log.warn("Attempted force logout, but key not found. userUuid={}", reqDto.userUuid());
            }
        } catch (Exception e) {
            log.error("Failed to delete RefreshToken from Redis. key={}", rootKey, e);
            throw new RuntimeException("Redis 삭제 실패", e);
        }
    }
}