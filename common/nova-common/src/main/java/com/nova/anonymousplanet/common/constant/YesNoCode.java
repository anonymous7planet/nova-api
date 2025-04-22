package com.nova.anonymousplanet.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.common.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.common.util.EnumUtils;

import javax.persistence.Converter;

public enum YesNoCode implements BaseEnum<String> {
    YES("Y", "Yes"),
    NO("N", "No");

    private final String code;
    private final String desc;

    YesNoCode(final String code, final String desc) {
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
    public static YesNoCode fromCode(String code) {
        return EnumUtils.fromCode(YesNoCode.class, code);
    }

    @Converter(autoApply = false)
    public static class YesNoCodeConverter extends BaseEnumConverter<YesNoCode, String> {
        public YesNoCodeConverter() {
            super(YesNoCode.class);
        }
    }
}
