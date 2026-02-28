package com.nova.anonymousplanet.core.exception.domain.common;

import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.NovaApplicationException;
import com.nova.anonymousplanet.core.exception.category.NotFoundException;
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

public class InvalidEnumCodeException extends NotFoundException {
    // 정적 팩토리 메서드 활용 (권장)
    public static InvalidEnumCodeException of(Class<?> enumType, Object code) {
        return new InvalidEnumCodeException(enumType, code);
    }

    public InvalidEnumCodeException(Class<?> enumType, Object code) {
        // 부모(NotFoundException -> BusinessException)에게 ErrorCode와 포맷팅된 메시지 전달
        super(CommonErrorCode.INVALID_ENUM_VALUE,
                String.format(CommonErrorCode.INVALID_ENUM_VALUE.getMessage(), enumType.getSimpleName(), code), null, null);
    }
}
