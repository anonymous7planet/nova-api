package com.nova.anonymousplanet.core.exception.user;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.user
 * fileName : UserNotFoundException
 * author : Jinhong Min
 * date : 2025-11-18
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-18      Jinhong Min      최초 생성
 * ==============================================
 */
public class UserNotFoundException extends UserException{
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
