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

import com.nova.anonymousplanet.auth.dto.v1.AuthDto;
import com.nova.anonymousplanet.auth.service.v1.AuthService;
import com.nova.anonymousplanet.core.dto.request.RestSingleRequest;
import com.nova.anonymousplanet.core.dto.response.RestEmptyResponse;
import com.nova.anonymousplanet.core.dto.response.RestSingleResponse;
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
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<RestEmptyResponse> signup(@RequestBody @Valid RestSingleRequest<AuthDto.SignupRequest> request) {
        authService.signup(request.getData());
        return ResponseEntity.ok(RestEmptyResponse.success("회원 가입 성공"));
    }

    /**
     * 로그인
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<RestSingleResponse<AuthDto.LoginResponse>> login(@RequestBody @Valid RestSingleRequest<AuthDto.LoginRequest> request) {
        AuthDto.LoginResponse response = authService.login(request.getData());
        return ResponseEntity.ok(RestSingleResponse.success(response, request.getRequestId(), request.getPath()));
    }
}
