package com.nova.anonymousplanet.security.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@Getter
@RequiredArgsConstructor
public enum UserRoleCode {

    SYSTEM("ROLE_SYSTEM", "시스템 관리자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    MANAGER("ROLE_MANAGER", "매니저"),
    USER("ROLE_USER", "사용자")
    ;

    private final String code;
    private final String desc;


    public static UserRoleCode from(String roleStr) {
        return Arrays.stream(values())
                .filter(r -> r.name().equalsIgnoreCase(roleStr) || r.code.equalsIgnoreCase(roleStr))
                .findFirst()
                .orElse(USER); // 기본값 설정
    }

}
