package com.nova.anonymousplanet.auth.service;

import com.nova.anonymousplanet.auth.dto.v1.UserAuthDto;
import com.nova.anonymousplanet.auth.dto.v1.TokenDto;
import com.nova.anonymousplanet.auth.dto.v1.command.UserAuthCommand;
import com.nova.anonymousplanet.auth.entity.UserEntity;
import com.nova.anonymousplanet.auth.exception.auth.LoginFailedException;
import com.nova.anonymousplanet.auth.exception.user.DuplicateEmailException;
import com.nova.anonymousplanet.auth.provider.crypto.EncryptionProvider;
import com.nova.anonymousplanet.auth.repository.UserRepository;
import com.nova.anonymousplanet.core.constant.NovaEventTypeCode;
import com.nova.anonymousplanet.core.constant.UserRoleCode;
import com.nova.anonymousplanet.core.event.email.EmailSendEvent;
import com.nova.anonymousplanet.core.event.NovaEvent;
import com.nova.anonymousplanet.core.event.email.InlineImage;
import com.nova.anonymousplanet.core.util.RecordMapper;
import com.nova.anonymousplanet.messaging.producer.NovaEventPublisher;
import com.nova.anonymousplanet.persistence.util.crypto.EncryptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
public class UserAuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;
    private final EncryptionProvider encryptionProvider;

    private final NovaEventPublisher novaEventPublisher;

    // 이메일 중복 검사
    @Transactional
    public void existsEmail(String email) throws Exception {
        String encodedEmail = EncryptionUtils.encrypt(email);
        if(userRepository.existsByEmailHash(encodedEmail)) {
            throw new DuplicateEmailException();
        }
    }

    @Transactional
    public void signup(UserAuthDto.SignupRequest request) {
        // 1. 중복 검사 (CI 해시 또는 이메일 해시 기준)
        validateDuplicateUser(request);

        // 2. 회원가입 프로세스
        persistUserWithRetry(request);

        // 3. 회원 가입 축하 이메일발송 - 이메일 발송은 내 소관이 아님. 이벤트만 던짐!
        EmailSendEvent payload = new EmailSendEvent(null, request.email(), "WELCOME_CONFIRM", RecordMapper.toMap(request), null, List.of(new InlineImage("logo.png", "logo")));

        // NovaEvent라는 전사 표준 규격으로 감싸서
        NovaEvent<EmailSendEvent> event = NovaEvent.of(NovaEventTypeCode.EMAIL_SEND_REQUESTED, payload);

        // 공통 발행기(Publisher)를 통해 카프카로 툭 던짐
        novaEventPublisher.publish(event.type().getTopic(), event);

    }

    /**
     * UUID 중복 예외 발생 시 재시도하는 로직
     */
    private UserEntity persistUserWithRetry(UserAuthDto.SignupRequest request){
        // 2. 데이터 암호화 및 해싱
        String emailHash = encryptionProvider.hashForSearch(request.email());
        String phoneHash = encryptionProvider.hashForSearch(request.phoneNumber());
        String ciHash = encryptionProvider.hashForSearch(request.ci()); // CI 해시 추가

        String encryptedEmail = encryptionProvider.encrypt(request.email());
        String encryptedPhoneNumber = encryptionProvider.encrypt(request.phoneNumber());
        String encryptedCi = encryptionProvider.encrypt(request.ci()); // CI 암호화 추가
        String encryptedName = encryptionProvider.encrypt(request.name()); // 이름
        String encryptedCity = encryptionProvider.encrypt(request.ci()); // 주소(시,도)
        String encryptedDistrict = encryptionProvider.encrypt(request.district()); // 주소(시,구)

        String encodedPassword = passwordEncoder.encode(request.password());

        int retryCount = 0;
        while (retryCount < 3) {
            try {
                // 새로운 UUID 생성
                String uuid = UUID.randomUUID().toString();

                // UserEntity 생성
                UserEntity user = UserEntity.createUser(
                        uuid, emailHash, phoneHash, ciHash,
                        encodedPassword, request.loginProvider(), UserRoleCode.USER,
                        request.isTermsAgreed(), request.isPrivacyAgreed(),
                        request.isLocationAgreed(), request.isMarketingAgreed()
                );

                // 연관관계 Command객체 생성
                UserAuthCommand.UserPrivacyCommand userPrivacyCommand = request.toPrivacyCommand(encryptedEmail, encryptedPhoneNumber, encryptedCi, encryptedName, encryptedCity, encryptedDistrict);
                UserAuthCommand.UserProfileCommand userProfileCommand = request.toProfileCommand();
                List<UserAuthCommand.UserProfileImageCommand> userProfileImageCommands = request.toProfileImagesCommand();
                List<UserAuthCommand.UserIntroductionCommand> userIntroductionCommands  = request.toIntroductionsCommand();

                // 연관관계 객체 생성
                user.registerUserInfo(UserAuthCommand.UserSignupCommand.of(userPrivacyCommand, userProfileCommand, userProfileImageCommands, userIntroductionCommands));

                // 저장 시도
                userRepository.save(user);
                return user;
            } catch (DataIntegrityViolationException e) {
                // UUID 중복(Unique Constraint) 예외 발생 시
                if (e.getMessage().contains("user_uuid")) {
                    retryCount++;
                    log.warn("UUID 충돌 발생! 새 UUID로 재시도 중... ({}회)", retryCount);
                    continue; // 다시 루프를 돌아 새로운 UUID 생성 후 시도
                }
                throw e; // UUID 중복이 아닌 다른 제약조건 위반이면 그대로 예외 던짐
            }
        }
        throw new RuntimeException("고유 식별자 생성 한도 초과로 가입에 실패했습니다.");
    }

    private void validateDuplicateUser(UserAuthDto.SignupRequest request) {
        // CI 해시로 중복 가입 여부 체크 (본인인증 기반이므로 가장 확실함)
        String ciHash = encryptionProvider.hashForSearch(request.ci());

        // FIXME: Exception 캐치 안됨
        if (userRepository.existsByCiHash(ciHash)) {
            throw new DuplicateEmailException();
        }

        // 이메일 중복 체크
        String emailHash = encryptionProvider.hashForSearch(request.email());
        if (userRepository.existsByEmailHash(emailHash)) {
            throw new DuplicateEmailException();
        }
    }

    /**
     * 로그인 처리
     * - 사용자 검증
     * - Access / Refresh Token 발급
     * - Redis에 RefreshToken 상태 저장
     */
    public UserAuthDto.LoginResponse login(UserAuthDto.LoginRequest request) {
        UserEntity user = userRepository.findByEmailHash(encryptionProvider.hashForSearch(request.email()))
            .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new LoginFailedException("패스워드 불일치");
        }

        if(!user.getStatus().isLoginAllowed()) {
            throw new LoginFailedException("회원 상태가 로그인을 허용하지 않은 상태");
        }


        // FIXME: 토큰 정보에 회원 구분값 넣기 userTypeCode
        TokenDto.IssueResponse token = tokenService.Issue(new TokenDto.IssueRequest(user.getId(), user.getUuid(), request.deviceId(), user.getRole(), user.getStatus()));

//        return new AuthDto.LoginResponse(user.getEmail(), user.getName(), user.getNickname(), user.getGender(), token);
        return null;
    }
}
