package com.nova.anonymousplanet.auth.controller.v1;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.controller
  fileName : UserController
  author : Jinhong Min
  date : 2025-10-28
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-28      Jinhong Min      최초 생성
  ==============================================
 */

import com.nova.anonymousplanet.auth.dto.v1.UserAuthDto;
import com.nova.anonymousplanet.auth.service.UserAuthService;
import com.nova.anonymousplanet.core.dto.v1.request.RestSingleRequest;
import com.nova.anonymousplanet.core.dto.v1.response.RestEmptyResponse;
import com.nova.anonymousplanet.core.dto.v1.response.RestSingleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
//    public ResponseEntity<RestEmptyResponse> signup(@RequestBody @Valid RestSingleRequest<UserAuthDto.SignupRequest> request) {
    public ResponseEntity<RestEmptyResponse> signup(@RequestBody RestSingleRequest<UserAuthDto.SignupRequest> request) {
        userAuthService.signup(request.getData());
        return ResponseEntity.ok(RestEmptyResponse.success("회원 가입 성공"));
    }

    /**
     * 로그인
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<RestSingleResponse<UserAuthDto.LoginResponse>> login(@RequestBody @Valid RestSingleRequest<UserAuthDto.LoginRequest> request) {
        UserAuthDto.LoginResponse response = userAuthService.login(request.getData());
        return ResponseEntity.ok(RestSingleResponse.success(response));
    }
}
