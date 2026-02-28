package com.nova.anonymousplanet.auth.exception.user;

import com.nova.anonymousplanet.auth.exception.AuthErrorCode;
import com.nova.anonymousplanet.core.exception.category.BusinessException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.user
 * fileName : DuplicateEmailException
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(AuthErrorCode.EMAIL_ALREADY_EXISTS);
    }

}
