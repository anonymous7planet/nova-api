package com.nova.anonymousplanet.gateway.dto;


/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.domain.jwt.dto
 * fileName : RefreshTokenStoreDto
 * author : Jinhong Min
 * date : 2025-04-22
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-22         Jinhong Min         최초 생성
 * ==============================================
 */
public record RefreshTokenStoreDto() {

    public record GetRequest(
        String userUuid,
        String deviceId
    ) {
        public String getKey() {
            return "refresh:" + this.userUuid + ":" + this.deviceId;
        }
    }

    public record GetResponse(
        Long userId,
        String userStatus
    ) {
    }

    public record ValidateRequest(
        String userUuid,
        String deviceId
    ) {
        public String getKey() {
            return "refresh:" + this.userUuid + ":" + this.deviceId;
        }
    }
}
