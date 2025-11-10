package com.nova.anonymousplanet.core.exception.user;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.exception.user
  fileName : UserRegisterException
  author : Jinhong Min
  date : 2025-11-03
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-03      Jinhong Min      최초 생성
  ==============================================
 */
public class UserRegistrationException extends UserException {
    public UserRegistrationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserRegistrationException() {
        this(ErrorCode.USER_REGISTER_BAD_REQUEST);
    }
}
