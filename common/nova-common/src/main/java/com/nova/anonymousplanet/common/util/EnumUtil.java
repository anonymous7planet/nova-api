package com.nova.anonymousplanet.common.util;

import com.nova.anonymousplanet.common.constant.BaseEnum;

import java.util.Arrays;

public class EnumUtil {

    public static <T extends Enum<T> & BaseEnum> T fromCode(Class<T> enumClass, String code) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code : " + code));
    }
}
