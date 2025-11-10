package com.nova.anonymousplanet.core.exception.user;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.BadRequestException;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.exception.user
  fileName : UserException
  author : Jinhong Min
  date : 2025-11-03
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-03      Jinhong Min      최초 생성
  ==============================================
 */
public class UserException extends BadRequestException {
    public UserException(ErrorCode errorCode, String message) {
        super(message, errorCode, errorCode.getTitleMessage(), errorCode.getDetailMessage());
    }

    public UserException(ErrorCode errorCode) {
        this(errorCode, "회원 정보에 문제가 발생했습니다.");
    }
}
