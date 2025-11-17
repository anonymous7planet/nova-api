package com.nova.anonymousplanet.auth.service.v1;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.application.service
  fileName : TokenService
  author : Jinhong Min
  date : 2025-05-16
  description : 토큰 발급, 재발급, 제거 정의
  userUuid 프론트에 저장
  devicedId localStorage저장
  refershToken cookie저장
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-05-16      Jinhong Min      최초 생성
  ==============================================
 */

import com.nova.anonymousplanet.auth.dto.v1.RefreshTokenStoreDto;
import com.nova.anonymousplanet.auth.dto.v1.TokenDto;
import com.nova.anonymousplanet.auth.entity.UserEntity;
import com.nova.anonymousplanet.auth.service.jwt.JwtRefreshTokenStore;
import com.nova.anonymousplanet.auth.service.jwt.JwtTokenProvider;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.token.TokenException;
import com.nova.anonymousplanet.core.exception.token.TokenValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider tokenProvider;
    private final JwtRefreshTokenStore tokenStore;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpireMillis;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpireMillis;


    public TokenDto.IssueResponse Issue(TokenDto.IssueRequest request) {
        return generateToken(request);
    }


    public TokenDto.ReIssueResponse reIssue(String refreshToken, TokenDto.ReIssueRequest request) {
        // 유효한 refreshToken인지 확인
        boolean tokenValid = tokenProvider.validateRefreshToken(refreshToken);
        if(!tokenValid) {
            throw new TokenException();
        }

        // redis에 토큰 정보 저장되어있는지 확인
        boolean storeValid = tokenStore.validate(RefreshTokenStoreDto.ValidateRequest.from(refreshToken, request));
        if(!storeValid) {
            throw new TokenException();
        }
        RefreshTokenStoreDto.GetResponse storeData = tokenStore.get(new RefreshTokenStoreDto.GetRequest(request.userUuid(), request.deviceId())).get();
        return new TokenDto.ReIssueResponse(generateToken(
            new TokenDto.IssueRequest(storeData.userId(), request.userUuid(), request.deviceId(), storeData.userRole(), storeData.userStatus())
        ));
    }

    public void deleteToken(String deviceId, TokenDto.DeleteRequest request) {
        boolean valid = tokenStore.validate(new RefreshTokenStoreDto.ValidateRequest(request.userUuid(), deviceId, request.refreshToken()));
        if(!valid){
            // 검증 실패
            throw new TokenValidationException();
        }
        tokenStore.delete(new RefreshTokenStoreDto.DeleteRequest(request.userUuid(), deviceId));
    }

    public void deleteAllToken(TokenDto.DeleteRequest request) {
        tokenStore.deleteAll(new RefreshTokenStoreDto.DeleteRequest(request.userUuid(), null));
    }


    /**
     * 토큰 생성 메소드
     * 로그인과 토큰 재발급시 모두 accessToken과 refreshToken다시 생성
     * accessToken과 refreshToken 생성 및 redis에 저장
     * @param request
     * @return
     */
    private TokenDto.IssueResponse generateToken(TokenDto.IssueRequest request) {
        String accessToken = tokenProvider.createAccessToken(request);
        String refreshToken = tokenProvider.createRefreshToken(request);
        // refreshToken저장
        tokenStore.store(RefreshTokenStoreDto.StoreRequest.from(request,refreshToken, refreshTokenExpireMillis));
        return new TokenDto.IssueResponse(accessToken, "Bearer", accessTokenExpireMillis, refreshToken, refreshTokenExpireMillis);
    }
}
