package com.nova.anonymousplanet.auth.service.v1;

import com.nova.anonymousplanet.auth.dto.v1.AuthDto;
import com.nova.anonymousplanet.auth.dto.v1.TokenDto;
import com.nova.anonymousplanet.auth.entity.UserEntity;
import com.nova.anonymousplanet.auth.repository.UserRepository;
import com.nova.anonymousplanet.core.constant.RoleCode;
import com.nova.anonymousplanet.core.constant.UserStatusCode;
import com.nova.anonymousplanet.core.constant.YesNoCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.user.UserLoginException;
import com.nova.anonymousplanet.core.exception.user.UserRegistrationException;
import com.nova.anonymousplanet.messaging.constant.EmailTemplateTypeCode;
import com.nova.anonymousplanet.messaging.model.EmailPayload;
import com.nova.anonymousplanet.messaging.model.InlineImage;
import com.nova.anonymousplanet.messaging.service.EmailAsyncService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.service
  fileName : AuthService
  author : Jinhong Min
  date : 2025-11-04
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-04      Jinhong Min      최초 생성
  ==============================================
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private final EmailAsyncService emailAsyncService;


    /**
     * 회원가입
     */
    @Transactional
    public void signup(AuthDto.SignupRequest request) {

        // 이메일 중복 검사
        if (userRepository.existsByEmail(request.email())) {
            throw new UserRegistrationException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }

        // 닉네임 중복 검사
        if (userRepository.existsByNickname(request.nickname())) {
            throw new UserRegistrationException(ErrorCode.USER_NICKNAME_ALREADY_EXISTS);
        }

        // 비밀번호 일치 검사
        if (!request.password().equals(request.passwordConfirm())) {
            throw new UserRegistrationException(ErrorCode.USER_PASSWORD_MISMATCH);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 엔티티 생성
        UserEntity user = UserEntity.create(
            request.email(),
            encodedPassword,
            request.nickname(),
            request.gender(),
            request.mbti(),
            request.bloodType(),
            RoleCode.USER,
            UserStatusCode.PENDING,
            YesNoCode.NO
        );
        // 저장
        userRepository.save(user);


        // 회원 가입 축하 이메일발송
        EmailTemplateTypeCode template = EmailTemplateTypeCode.WELCOME;
        Map<String, Object> variables = new HashMap<>();
        variables.put("nickname", request.name());

        List<InlineImage> images = new ArrayList<>();

        images.add(new InlineImage("test.jpg", "test"));


        emailAsyncService.sendAsync(
            new EmailPayload(
                request.email(),
                String.format(template.getTitle(), request.name()),
                template,
                variables,
                null,
                images
            )
        );
    }

    /**
     * 로그인 처리
     * - 사용자 검증
     * - Access / Refresh Token 발급
     * - Redis에 RefreshToken 상태 저장
     */
    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        // FIXME: 관리자 로그인과 회원 로그인 구분 필요
        UserEntity user = userRepository.findByEmail(request.email())
            .orElseThrow(UserLoginException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserLoginException();
        }

        if(!user.getStatus().isLoginAllowed()) {
            throw new UserLoginException();
        }


        // FIXME: 토큰 정보에 회원 구분값 넣기 userTypeCode
        TokenDto.IssueResponse token = tokenService.Issue(new TokenDto.IssueRequest(user.getId(), user.getUuid(), request.deviceId(), user.getRole(), user.getStatus()));

        return new AuthDto.LoginResponse(user.getEmail(), user.getName(), user.getNickname(), user.getGender(), token);
    }
}
