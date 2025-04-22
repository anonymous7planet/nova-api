package com.nova.anonymousplanet.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.common.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.common.exception.BadRequestException;
import com.nova.anonymousplanet.common.util.EnumUtils;

import javax.persistence.Converter;

public enum RoleGroupCode implements BaseEnum<String> {

    SYSTEM("SYSTEM_ADMIN_GROUP", "시스템 관리자"),
    ADMIN("ADMIN_GROUP", "관리자"),
    MANAGER("MANAGER_GROUP", "매니저"),
    VIP_MEMBER("VIP_MEMBER_GROUP", "VIP회원"),
    MEMBER("MEMBER_GROUP", "일반회원")
    ;

    private final String code;
    private final String desc;

    RoleGroupCode(final String code, final String desc) {
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
    public static RoleGroupCode fromCode(String code) {
        return EnumUtils.fromCode(RoleGroupCode.class, code);
    }

    @Converter(autoApply = false)
    public static class RoleGroupCodeConverter extends BaseEnumConverter<RoleGroupCode, String> {
        public RoleGroupCodeConverter() {
            super(RoleGroupCode.class);
        }
    }
}
