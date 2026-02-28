package com.nova.anonymousplanet.auth.exception.user;

import com.nova.anonymousplanet.auth.exception.AuthErrorCode;
import com.nova.anonymousplanet.core.exception.category.NotFoundException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.user
 * fileName : UserNotFoundException
 * author : Jinhong Min
 * date : 2026-02-14
 * description : 
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String logMessage) {
        super(AuthErrorCode.USER_NOT_FOUND, logMessage);
    }

    public UserNotFoundException() {
        this(null);
    }

}
