package com.nova.anonymousplanet.auth.dto.v1;

import com.nova.anonymousplanet.core.constant.RoleCode;
import com.nova.anonymousplanet.core.constant.UserStatusCode;
import jakarta.validation.constraints.NotBlank;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.dto
  fileName : TokenDto
  author : Jinhong Min
  date : 2025-11-04
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-04      Jinhong Min      최초 생성
  ==============================================
 */
public record TokenDto() {

    public record IssueRequest(
        Long userId,
        String userUuid,
        String deviceId,
        RoleCode role,
        UserStatusCode userStatus
    ) {
    }

    public record IssueResponse(
        String accessToken,     // 실제 API 호출에 사용될 Access Token (JWT)
        String tokenType,       // 토큰 유형 (항상 "Bearer"로 설정)
        long expiresIn,         // Access Token의 남은 유효 시간 (초 단위)
        String refreshToken,    // Access Token 만료 시 재발급에 사용될 Refresh Token
        long refreshTokenExpiresIn // Refresh Token의 남은 유효 시간 (초 단위)
    ) {}

    public record ReIssueRequest(
        @NotBlank(message = "회원 uuid는 필수 값 입니다.")
        String userUuid,
        @NotBlank(message = "deviceId는 필수 값 입니다.")
        String deviceId,
        @NotBlank(message = "refreshToken은 필수 값 입니다.")
        String refreshToken
    ) {
    }

    public record DeleteRequest(
        @NotBlank(message = "회원 uuid는 필수 값 입니다.")
        String userUuid,

        String refreshToken
    ) {
    }
}
