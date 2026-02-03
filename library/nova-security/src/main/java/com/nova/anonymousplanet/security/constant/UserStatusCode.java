package com.nova.anonymousplanet.security.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@Getter
@RequiredArgsConstructor
public enum UserStatusCode {

    PENDING("PENDING", "가입 대기(승인전)", false),
    NORMAL("NORMAL", "정상 이용 중", true),
    SUSPENDED("SUSPENDED", "이용 정지", false),
    WITHDRAWN("WITHDRAWN", "탈퇴", false)

    ;

    private final String code;
    private final String desc;
    private final boolean isLoginAllowed;

    public boolean isLoginAllowed() {
        return this.isLoginAllowed;
    }

    public static UserStatusCode from(String statusStr) {
        return Arrays.stream(values())
                .filter(r -> r.code.equalsIgnoreCase(statusStr))
                .findFirst()
                .orElse(null); // 기본값 설정
    }

}
