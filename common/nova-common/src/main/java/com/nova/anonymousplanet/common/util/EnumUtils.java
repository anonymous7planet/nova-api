package com.nova.anonymousplanet.common.util;

import com.nova.anonymousplanet.common.constant.BaseEnum;
import com.nova.anonymousplanet.common.constant.ErrorCode;
import com.nova.anonymousplanet.common.exception.EnumCodeNotFoundException;

import java.util.Arrays;
import java.util.Objects;

public class EnumUtils {

    public static <E extends Enum<E> & BaseEnum<T> , T> E fromCode(Class<E> enumType, T code) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new EnumCodeNotFoundException(
                        String.format(ErrorCode.INVALID_ENUM_CODE.getDetailMessage(), enumType.getSimpleName(), code)));
    }
}
