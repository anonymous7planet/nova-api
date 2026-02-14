package com.nova.anonymousplanet.core.exception.domain.user;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.ResourceNotFoundException;
import lombok.Getter;

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

@Getter
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(final String logMessage) {
        super(ErrorCode.USER_NOT_FOUND, logMessage);
    }
}
