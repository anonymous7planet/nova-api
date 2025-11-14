package com.nova.anonymousplanet.core.exception.token;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.token
 * fileName : TokenValidationException
 * author : Jinhong Min
 * date : 2025-11-14
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-14      Jinhong Min      최초 생성
 * ==============================================
 */
public class TokenValidationException extends TokenException {

    public TokenValidationException() {
        super(ErrorCode.TOKEN_ILLEGAL, "유효 하지 않은 토큰정보입니다.");
    }
}
