package com.nova.anonymousplanet.core.exception.domain.common;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.InvalidRequestException;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.common
 * fileName : InvalidParameterException
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
public class InvalidParameterException extends InvalidRequestException {

    public InvalidParameterException() {
        super(ErrorCode.INVALID_PARAMETER);
    }
}
