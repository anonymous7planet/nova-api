package com.nova.anonymousplanet.auth.application.service;

import com.nova.anonymousplanet.auth.application.dto.SignupDto;
import com.nova.anonymousplanet.auth.application.port.input.SignUpUseCase;
import com.nova.anonymousplanet.auth.application.port.output.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.application.service
 * fileName : SignUpService
 * author : Jinhong Min
 * date : 2025-05-01
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-05-01      Jinhong Min      최초 생성
 * ==============================================
 */
@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final UserPersistencePort userPersistencePort;

    @Override
    public void signUp(SignupDto.SignupRequestDto reqDto) {
        userPersistencePort.saveUser(reqDto);
    }
}
