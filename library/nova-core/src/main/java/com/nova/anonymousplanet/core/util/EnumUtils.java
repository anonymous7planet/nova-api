package com.nova.anonymousplanet.core.util;

import com.nova.anonymousplanet.core.constant.BaseEnum;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.domain.common.InvalidEnumCodeException;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

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

//    public static <E extends Enum<E> & BaseEnum<T>, T> E fromCode(Class<E> enumType, T code) {
//        return Arrays.stream(enumType.getEnumConstants())
//                .filter(e -> Objects.equals(e.getCode(), code))
//                .findFirst()
//                .orElseThrow(() -> {
//                    // 허용되는 값들의 목록을 추출 (예: [M, F])
//                    String allowedCodes = Arrays.stream(enumType.getEnumConstants())
//                            .map(e -> String.valueOf(e.getCode()))
//                            .collect(Collectors.joining(", "));
//
//                    return new InvalidEnumCodeException(
//                            ErrorCode.INVALID_ENUM_VALUE,
//                            String.format("[%s] 필드에 잘못된 값 [%s]이(가) 입력되었습니다. 허용되는 값: [%s]",
//                                    enumType.getSimpleName(), code, allowedCodes)
//                    );
//                });
//    }
}
