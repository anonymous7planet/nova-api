package com.nova.anonymousplanet.core.helper;

import com.nova.anonymousplanet.core.constant.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<? extends BaseEnum>> {
    @Override
    public <T extends Enum<? extends BaseEnum>> Converter<String, T> getConverter(Class<T> targetType) {
        return null;
    }
}
