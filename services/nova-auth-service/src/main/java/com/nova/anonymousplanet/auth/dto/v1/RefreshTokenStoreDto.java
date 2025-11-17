package com.nova.anonymousplanet.auth.dto.v1;

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
        Long userId,
        String userUuid,
        String deviceId,
        String refreshToken,
        RoleCode role,
        UserStatusCode userStatus,
        Long expirationSeconds
    ) {
        public String getRootKey() {
            return "refresh:"+this.userUuid;
        }

        // 💡 정적 팩토리 메서드: IssueRequest와 추가 필드를 받아 StoreRequest를 생성합니다.
        public static StoreRequest from(
            TokenDto.IssueRequest issueRequest,
            String refreshToken,
            Long expirationSeconds
        ) {
            return new StoreRequest(
                issueRequest.userId(),
                issueRequest.userUuid(),
                issueRequest.deviceId(),
                refreshToken,
                issueRequest.role(),
                issueRequest.userStatus(),
                expirationSeconds
            );
        }
    }

    public record GetRequest(
        String userUuid,
        String deviceId
    ) {
        public String getRootKey() {
            return "refresh:" + this.userUuid;
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
            return "refresh:" + this.userUuid;
        }

        public static ValidateRequest from(String refreshToken, TokenDto.ReIssueRequest request){
            return new ValidateRequest(
                request.userUuid(),
                request.deviceId(),
                refreshToken
            );
        }
    }

    public record DeleteRequest (
        String userUuid,
        String deviceId
    ) {
        public String getRootKey() {
            return "refresh:" + this.userUuid;
        }
    }
}
