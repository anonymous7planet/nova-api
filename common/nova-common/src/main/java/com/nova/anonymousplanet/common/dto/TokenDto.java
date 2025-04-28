package com.nova.anonymousplanet.common.dto;

import com.nova.anonymousplanet.common.constant.RoleCode;
import com.nova.anonymousplanet.common.constant.RoleGroupCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TokenCreateRequest {
        private String uuid;
        private RoleGroupCode roleGroup;
        private RoleCode role;
        private String deviceId;

        @Builder
        public TokenCreateRequest(String uuid, RoleGroupCode roleGroup, RoleCode role, String deviceId) {
            this.uuid = uuid;
            this.roleGroup = roleGroup;
            this.role = role;
            this.deviceId = deviceId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TokenCreateResponse {
        private String grantType;
        private String accessToken;
        private Long accessTokenExpiresAt; // 만료 시간은 밀리세컨드 단위가 아니라 초단위로
        private String refreshToken;
        private Long refreshTokenExpiresAt; // 만료 시간은 밀리세컨드 단위가 아니라 초단위로

        @Builder
        public TokenCreateResponse(String grantType, String accessToken, Long accessTokenExpiresAt, String refreshToken, Long refreshTokenExpiresAt) {
            this.grantType = grantType;
            this.accessToken = accessToken;
            this.accessTokenExpiresAt = accessTokenExpiresAt;
            this.refreshToken = refreshToken;
            this.refreshTokenExpiresAt = refreshTokenExpiresAt;
        }
    }
}
