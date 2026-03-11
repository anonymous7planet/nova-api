package com.nova.anonymousplanet.gateway.configuration.security.model;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration.security.model
 * fileName : UserPrincipal
 * author : Jinhong Min
 * date : 2026-03-11
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-11      Jinhong Min      최초 생성
 * ==============================================
 */
public record UserPrincipal(
        Long userId,      // Redis에서 가져온 내부 DB ID
        String uuid,      // 외부 노출용 UUID
        String role,
        String userStatus, // 유저 상태 (예: ACTIVE, BLOCK)
        String deviceId
) {
}
