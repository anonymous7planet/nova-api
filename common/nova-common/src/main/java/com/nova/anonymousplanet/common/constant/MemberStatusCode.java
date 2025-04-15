package com.nova.anonymousplanet.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.common.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.common.exception.EnumCodeNotFoundException;
import com.nova.anonymousplanet.common.util.EnumUtils;

import javax.persistence.Converter;

public enum MemberStatusCode implements BaseEnum<String> {
    PENDING("PENDING", "가입 대기 상태 회원"),
    ACTIVE("ACTIVE", "활성 회원"),
    SUSPENDED("SUSPENDED", "이용 정지 회원"),
    WITHDRAWN("WITHDRAWN", "탈퇴 회원");

    private final String code;
    private final String desc;

    MemberStatusCode(final String code, final String desc) {
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
    public static MemberStatusCode creator(String code) {
        return EnumUtils.fromCode(MemberStatusCode.class, code);
    }


    @Converter(autoApply = false)
    public static class MemberStatusCodeConverter extends BaseEnumConverter<MemberStatusCode, String> {
        public MemberStatusCodeConverter() {
            super(MemberStatusCode.class);
        }
    }
}
