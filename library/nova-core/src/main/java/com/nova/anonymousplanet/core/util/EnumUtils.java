package com.nova.anonymousplanet.core.util;

import com.nova.anonymousplanet.core.constant.BaseEnum;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.common.EnumCodeNotFoundException;

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
                .orElseThrow(() -> new EnumCodeNotFoundException(
                        String.format(ErrorCode.INVALID_ENUM_CODE.getDetailMessage(), enumType.getSimpleName(), code)));
    }
}
