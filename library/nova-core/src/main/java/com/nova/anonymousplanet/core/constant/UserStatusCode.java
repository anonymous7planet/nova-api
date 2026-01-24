package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;


public enum UserStatusCode implements BaseEnum<String> {
    PENDING("PENDING", "가입 대기(승인전)", false),
    NORMAL("NORMAL", "정상 이용 중", true),
    SUSPENDED("SUSPENDED", "이용 정지", false),
    WITHDRAWN("WITHDRAWN", "탈퇴", false);
    ;

    private final String code;
    private final String desc;
    private final boolean isLoginAllowed;

    UserStatusCode(final String code, final String desc, final boolean isLoginAllowed) {
        this.code = code;
        this.desc = desc;
        this.isLoginAllowed = isLoginAllowed;
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

    public boolean isLoginAllowed() {
        return this.isLoginAllowed;
    }

    @JsonCreator
    public static UserStatusCode fromCode(String code) {
        return EnumUtils.fromCode(UserStatusCode.class, code);
    }

    @Converter(autoApply = true)  // autoApply를 true로 하면 Entity에 @Convert를 안 붙여도 자동 적용됩니다.
    public static class UserStatusCodeConverter extends BaseEnumConverter<UserStatusCode, String> {
        public UserStatusCodeConverter() {
            super(UserStatusCode.class);
        }
    }
}
