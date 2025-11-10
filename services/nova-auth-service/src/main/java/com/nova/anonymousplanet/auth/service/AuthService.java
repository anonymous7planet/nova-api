package com.nova.anonymousplanet.auth.service;

import com.nova.anonymousplanet.auth.dto.AuthDto;
import com.nova.anonymousplanet.auth.entity.UserEntity;
import com.nova.anonymousplanet.auth.repository.UserRepository;
import com.nova.anonymousplanet.core.constant.RoleCode;
import com.nova.anonymousplanet.core.constant.UserStatusCode;
import com.nova.anonymousplanet.core.constant.YesNoCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.user.UserRegistrationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    }
}
