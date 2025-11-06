package com.nova.anonymousplanet.auth.service;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.application.service
  fileName : TokenService
  author : Jinhong Min
  date : 2025-05-16
  description : 토큰 발급, 재발급, 제거 정의
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-05-16      Jinhong Min      최초 생성
  ==============================================
 */

import com.nova.anonymousplanet.auth.dto.TokenDto;
import com.nova.anonymousplanet.auth.service.jwt.JwtRefreshTokenStore;
import com.nova.anonymousplanet.auth.service.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider tokenProvider;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpireMillis;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpireMillis;

    public TokenDto.IssueResponse createAccessToken(TokenDto.IssueRequest request) {
        String accessToken = tokenProvider.createAccessToken(request);
        return new TokenDto.IssueResponse(accessToken, accessTokenExpireMillis);
    }

    public TokenDto.IssueResponse createRefreshToken(TokenDto.IssueRequest request) {
        String refreshToken = tokenProvider.createRefreshToken(request);
        return new TokenDto.IssueResponse(refreshToken, refreshTokenExpireMillis);
    }
}
