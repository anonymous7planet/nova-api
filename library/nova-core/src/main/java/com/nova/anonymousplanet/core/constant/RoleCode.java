package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;


public enum RoleCode implements BaseEnum<String> {

    SUPER_ADMIN("ROLE_SUPER_ADMIN", "최고관리자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    MANAGER("ROLE_MANAGER", "매니저"),
    ANALYST("ROLE_ANALYST", "분석가"),
    USER("ROLE_USER", "사용자")
    ;

    private final String code;
    private final String desc;

    RoleCode(final String code, final String desc) {
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
    public static RoleCode fromCode(String code) {
        return EnumUtils.fromCode(RoleCode.class, code);
    }

    @Converter(autoApply = false)
    public static class RoleCodeConverter extends BaseEnumConverter<RoleCode, String> {
        public RoleCodeConverter() {
            super(RoleCode.class);
        }
    }
}
