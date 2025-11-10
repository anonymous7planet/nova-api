package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;

public enum GenderCode implements BaseEnum<String> {

    MALE("M", "남성"),
    FEMALE("F", "여성"),
    OTHER("O", "기타")
    ;

    private final String code;
    private final String desc;

    GenderCode(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @JsonCreator
    public static GenderCode fromCode(String code) {
        return EnumUtils.fromCode(GenderCode.class, code);
    }

    @Converter(autoApply = false)
    public static class GenderCodeConverter extends BaseEnumConverter<GenderCode, String> {
        public GenderCodeConverter() {
            super(GenderCode.class);
        }
    }
}
