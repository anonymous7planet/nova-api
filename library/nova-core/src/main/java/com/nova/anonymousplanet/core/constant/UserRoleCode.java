package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;


public enum UserRoleCode implements BaseEnum<String> {

    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "사용자")
    ;

    private final String code;
    private final String desc;

    UserRoleCode(final String code, final String desc) {
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
    public static UserRoleCode fromCode(String code) {
        return EnumUtils.fromCode(UserRoleCode.class, code);
    }

    @Converter(autoApply = false)
    public static class RoleCodeConverter extends BaseEnumConverter<UserRoleCode, String> {
        public RoleCodeConverter() {
            super(UserRoleCode.class);
        }
    }
}
