package com.nova.anonymousplanet.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.common.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.common.util.EnumUtils;
import jakarta.persistence.Converter;


public enum UserStatusCode implements BaseEnum<String> {
    PENDING("PENDING", "가입 대기 상태"),
    FRIEND_ACTIVE("F_ACTIVE", "친구모드 활성"),
    MATCH_PENDING("M_PENDING", "맞선모드 가입 대기 상태"),
    MATCH_ACTIVE("M_ACTIVE", "맞선모드 활성"),
    SUSPENDED("SUSPENDED", "이용 정지"),
    BANNED("BANNED", "추방"),
    WITHDRAWN("WITHDRAWN", "탈퇴")
    ;

    private final String code;
    private final String desc;

    UserStatusCode(final String code, final String desc) {
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
    public static UserStatusCode fromCode(String code) {
        return EnumUtils.fromCode(UserStatusCode.class, code);
    }

    @Converter(autoApply = false)
    public static class UserStatusCodeConverter extends BaseEnumConverter<UserStatusCode, String> {
        public UserStatusCodeConverter() {
            super(UserStatusCode.class);
        }
    }
}
