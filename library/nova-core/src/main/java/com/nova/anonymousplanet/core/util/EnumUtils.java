package com.nova.anonymousplanet.core.util;

import com.nova.anonymousplanet.core.constant.BaseEnum;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.domain.common.InvalidEnumCodeException;

import java.util.Arrays;
import java.util.Objects;

public class EnumUtils {

    private EnumUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <E extends Enum<E> & BaseEnum<T> , T> E fromCode(Class<E> enumType, T code) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumCodeException(
                        String.format(ErrorCode.INVALID_ENUM_VALUE.getDetailMessage(), enumType.getSimpleName(), code)));
    }
}
