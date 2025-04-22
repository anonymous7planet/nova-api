package com.nova.anonymousplanet.auth.domain.jwt.dto;

import com.nova.anonymousplanet.common.constant.RoleCode;
import com.nova.anonymousplanet.common.constant.RoleGroupCode;
import lombok.Builder;
import lombok.Getter;

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
public class RefreshTokenStoreDto {

    @Getter
    @Builder
    public static class StoreRequest {
        private final String uuid;
        private final String deviceId;
        private final String refreshToken;
        private final RoleGroupCode roleGroup;
        private final RoleCode role;
        private final Long userId;
        private final Long expirationSeconds;


        public String getKey() {
            return "refresh:"+this.uuid + ":" + this.deviceId;
        }
    }

    @Getter
    @Builder
    public static class GetRequest {
        private final String uuid;
        private final String deviceId;

        public String getKey() {
            return "refresh:" + this.uuid + ":" + this.deviceId;
        }
    }

    @Getter
    public static class GetResponse {
        private final String refreshToken;
        private final RoleGroupCode roleGroup;
        private final RoleCode role;
        private final Long userId;

        @Builder
        public GetResponse(String refreshToken, RoleGroupCode roleGroup, RoleCode role, Long userId) {
            this.refreshToken = refreshToken;
            this.roleGroup = roleGroup;
            this.role = role;
            this.userId = userId;
        }
    }

    @Getter
    @Builder
    public static class DeleteRequest {
        private final String uuid;
        private final String deviceId;

        public String getKey() {
            return "refresh:" + this.uuid + ":" + this.deviceId;
        }
    }


    @Getter
    @Builder
    public static class ValidateRequest {
        private final String uuid;
        private final String deviceId;
        private final String refreshToken;

        public String getKey() {
            return "refresh:" + this.uuid + ":" + this.deviceId;
        }
    }
}
