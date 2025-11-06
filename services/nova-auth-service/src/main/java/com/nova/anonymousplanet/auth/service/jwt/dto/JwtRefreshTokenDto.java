package com.nova.anonymousplanet.auth.service.jwt.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.service.jwt.dto
  fileName : JwtRefreshTokenDto
  author : Jinhong Min
  date : 2025-10-31
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-31      Jinhong Min      최초 생성
  ==============================================
 */
public record JwtRefreshTokenDto(
    String userUuid,       // 회원 UUID
    String deviceId,       // 디바이스 ID
    String refreshToken,   // Refresh Token
    LocalDateTime expiresAt, // 만료 시각
    boolean revoked         // 토큰 폐기 여부
    ) implements Serializable {

    public static JwtRefreshTokenDto create(String userUuid, String deviceId, String refreshToken, LocalDateTime expiresAt) {
        return new JwtRefreshTokenDto(userUuid, deviceId, refreshToken, expiresAt, false);
    }

    public JwtRefreshTokenDto revoke() {
        return new JwtRefreshTokenDto(userUuid, deviceId, refreshToken, expiresAt, true);
    }
}