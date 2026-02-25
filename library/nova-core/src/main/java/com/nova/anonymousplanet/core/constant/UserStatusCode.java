package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements BaseEnum<String> {
    PENDING("PENDING", "가입 대기(승인전)", false),
    NORMAL("NORMAL", "정상 이용 중", true),
    SUSPENDED("SUSPENDED", "이용 정지", false),
    WITHDRAWN("WITHDRAWN", "탈퇴", false);
    ;

    private final String code;
    private final String desc;
    private final boolean isLoginAllowed;

    @Override
    public String getName() {
        return this.name();
    }

    public boolean isLoginAllowed() {
        return this.isLoginAllowed;
    }

    @JsonCreator
    public static UserStatusCode fromCode(String code) {
        return EnumUtils.fromCode(UserStatusCode.class, code);
    }
}
