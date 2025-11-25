package com.nova.anonymousplanet.core.exception.user;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.exception.user
  fileName : UserLoginException
  author : Jinhong Min
  date : 2025-11-03
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-03      Jinhong Min      최초 생성
  ==============================================
 */
public class UserLoginException extends UserException {

    public UserLoginException() {
        super(ErrorCode.USER_LOGIN);
    }

}
