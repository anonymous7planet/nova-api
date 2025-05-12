package com.nova.anonymousplanet.auth.application.port.input;

import com.nova.anonymousplanet.auth.application.dto.SignupDto;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.application.port.input
 * fileName : SignUpUseCase
 * author : Jinhong Min
 * date : 2025-05-01
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-05-01      Jinhong Min      최초 생성
 * ==============================================
 */
public interface SignUpUseCase {
    void signUp(SignupDto.SignupRequestDto reqDto);
}
