package com.nova.anonymousplanet.core.exception.common;

import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;
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
public class InvalidParameterException extends NovaApplicationException {

    public InvalidParameterException() {
        super(CommonErrorCode.INVALID_PARAMETER);
    }
}
