package com.nova.anonymousplanet.web.configuration.converter;

import com.nova.anonymousplanet.core.constant.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<? extends BaseEnum>> {
    @Override
    public <T extends Enum<? extends BaseEnum>> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    /**
     * 실제 변환 로직을 담당하는 내부 정적 클래스
     */
    private static final class StringToEnumConverter<T extends Enum<? extends BaseEnum>> implements Converter<String, T> {

        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (!StringUtils.hasText(source)) {
                return null;
            }

            // Enum 상수 중 BaseEnum.getCode() 값이 입력값과 일치하는 것을 찾습니다.
            // 대소문자 구분이 필요 없다면 source.trim().toUpperCase() 등을 고려하세요.
            return Arrays.stream(enumType.getEnumConstants())
                    .filter(e -> ((BaseEnum) e).getCode().equals(source.trim()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("Invalid Enum code [%s] for type [%s]", source, enumType.getSimpleName())
                    ));
        }
    }
}
