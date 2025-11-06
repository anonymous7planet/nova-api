package com.nova.anonymousplanet.common.exception.common;

import com.nova.anonymousplanet.common.constant.error.ErrorCode;
import com.nova.anonymousplanet.common.exception.BadRequestException;

public class EnumCodeNotFoundException extends BadRequestException {
    public EnumCodeNotFoundException(String detailMessage) {
        super("코드 정보를 찾을 수 없습니다.", ErrorCode.INVALID_ENUM_CODE, "잘못된 코드 값 입니다", detailMessage);
    }
}
