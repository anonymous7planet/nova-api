package com.nova.anonymousplanet.core.exception.token;


import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.BadRequestException;

public class TokenException extends BadRequestException {
    public TokenException(ErrorCode errorCode, String message) {
        super(message, errorCode, errorCode.getTitleMessage(), errorCode.getDetailMessage());
    }

    public TokenException(ErrorCode errorCode) {
        super("토큰 정보에 문제가 있습니다. 다시 로그인이 필요합니다.", errorCode, errorCode.getTitleMessage(), errorCode.getDetailMessage());
    }
}
