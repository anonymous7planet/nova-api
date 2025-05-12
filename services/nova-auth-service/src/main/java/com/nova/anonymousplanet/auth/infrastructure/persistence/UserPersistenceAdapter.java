package com.nova.anonymousplanet.auth.infrastructure.persistence;

import com.nova.anonymousplanet.auth.application.dto.SignupDto;
import com.nova.anonymousplanet.auth.application.port.output.UserPersistencePort;
import com.nova.anonymousplanet.auth.domain.user.UserEntity;
import com.nova.anonymousplanet.auth.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.infrastructure.persistence
 * fileName : UserPersistenceAdapter
 * author : Jinhong Min
 * date : 2025-05-01
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-05-01      Jinhong Min      최초 생성
 * ==============================================
 */
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepository userRepository;

    @Override
    public void saveUser(SignupDto.SignupRequestDto reqDto) {
        UserEntity user = UserEntity.builder().build();
    }
}
