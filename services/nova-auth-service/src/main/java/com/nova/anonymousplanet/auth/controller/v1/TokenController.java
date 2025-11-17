package com.nova.anonymousplanet.auth.controller.v1;

import com.nova.anonymousplanet.auth.dto.v1.TokenDto;
import com.nova.anonymousplanet.auth.service.v1.TokenService;
import com.nova.anonymousplanet.core.dto.request.RestSingleRequest;
import com.nova.anonymousplanet.core.dto.response.RestEmptyResponse;
import com.nova.anonymousplanet.core.dto.response.RestSingleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.controller.v1
 * fileName : TokenController
 * author : Jinhong Min
 * date : 2025-11-11
 * description :
 * userUuid = in-memory저장
 * accessToken = in-memory 저장
 * refreshToken = cookie저장(HttpOnly)
 * deviceId = 로컬 스토리지 저장
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-11      Jinhong Min      최초 생성
 * ==============================================
 */
@RestController
@RequestMapping("/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;


    /**
     * 토큰 재발급
     * @param request
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<RestSingleResponse<TokenDto.ReIssueResponse>> refresh(
        @CookieValue(value = "refreshToken", required = true) String refreshToken,
        @RequestBody @Valid RestSingleRequest<TokenDto.ReIssueRequest> request) {
        return ResponseEntity.ok(
            RestSingleResponse.success(tokenService.reIssue(refreshToken, request.getData()), request.getRequestId(),
                request.getPath()));
    }

    /**
     * redis에 저장된 특정 사용자의 특정 기기의 refreshToken삭제
     * @param deviceId
     * @param request
     * @return
     */
    @DeleteMapping("/refresh/{deviceId}")
    public ResponseEntity<RestEmptyResponse> delete(@PathVariable String deviceId, @RequestBody @Valid RestSingleRequest<TokenDto.DeleteRequest> request) {
        tokenService.deleteToken(deviceId, request.getData());
        return ResponseEntity.ok(RestEmptyResponse.success());
    }

    /**
     * redis에 저장된 특정 사용자의 refreshToken 모두 삭제
     * @param request
     * @return
     */
    @DeleteMapping("/refresh")
    public ResponseEntity<RestEmptyResponse> deleteAll(@RequestBody @Valid RestSingleRequest<TokenDto.DeleteRequest> request) {
        tokenService.deleteAllToken(request.getData());
        return ResponseEntity.ok(RestEmptyResponse.success());
    }
}