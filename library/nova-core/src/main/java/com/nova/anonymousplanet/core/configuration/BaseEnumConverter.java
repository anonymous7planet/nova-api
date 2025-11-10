package com.nova.anonymousplanet.core.configuration;

import com.nova.anonymousplanet.core.constant.BaseEnum;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public abstract class BaseEnumConverter<E extends Enum<E> & BaseEnum<T>, T> implements AttributeConverter<E, T> {

    private final Class<E> enumClass;

    protected BaseEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public E convertToEntityAttribute(T dbData) {
        return dbData != null ? EnumUtils.fromCode(enumClass, dbData) : null;
    }
}