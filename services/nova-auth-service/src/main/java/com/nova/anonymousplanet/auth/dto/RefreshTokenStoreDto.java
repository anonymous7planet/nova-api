package com.nova.anonymousplanet.auth.dto;

import com.nova.anonymousplanet.core.constant.RoleCode;
import com.nova.anonymousplanet.core.constant.UserStatusCode;

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

    public record StoreRequest(
        String userUuid,
        String deviceId,
        Long userId,
        String refreshToken,
        RoleCode role,
        UserStatusCode userStatus,
        Long expirationSeconds
    ) {
        public String getKey() {
            return "refresh:"+this.userUuid + ":" + this.deviceId;
        }
    }

    public record GetRequest(
        String userUuid,
        String deviceId
    ) {
        public String getKey() {
            return "refresh:" + this.userUuid + ":" + this.deviceId;
        }
    }

    public record GetResponse(
        String userUuid,
        String deviceId,
        Long userId,
        String refreshToken,
        RoleCode userRole,
        UserStatusCode userStatus
    ) {
    }

    public record ValidateRequest(
        String userUuid,
        String deviceId,
        String refreshToken
    ) {
        public String getKey() {
            return "refresh:" + this.userUuid + ":" + this.deviceId;
        }
    }

    public record DeleteRequest (
        String userUuid,
        String deviceId
    ) {
        public String getKey() {
            return "refresh:" + this.userUuid + ":" + this.deviceId;
        }
    }
}
