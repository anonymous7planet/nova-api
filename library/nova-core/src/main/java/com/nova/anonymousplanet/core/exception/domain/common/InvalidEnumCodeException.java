package com.nova.anonymousplanet.core.exception.domain.common;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.InvalidRequestException;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.common
 * fileName : InvalidEnumCodeException
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * [Phase 3] exception.domain.common: 유효하지 않은 Enum 코드값이 전달된 경우
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
public class InvalidEnumCodeException extends InvalidRequestException {

    public InvalidEnumCodeException() {
        super(ErrorCode.INVALID_ENUM);
    }
    public InvalidEnumCodeException(final String detailMessage) {
        super(ErrorCode.INVALID_ENUM_VALUE, detailMessage, ErrorCode.INVALID_ENUM_VALUE.getTitleMessage(), detailMessage);
    }
}
