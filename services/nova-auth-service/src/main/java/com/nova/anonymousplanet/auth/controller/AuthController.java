package com.nova.anonymousplanet.auth.controller;

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

import com.nova.anonymousplanet.auth.dto.AuthDto;
import com.nova.anonymousplanet.auth.service.AuthService;
import com.nova.anonymousplanet.common.dto.request.RestSingleRequest;
import com.nova.anonymousplanet.common.dto.response.RestEmptyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
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
}
