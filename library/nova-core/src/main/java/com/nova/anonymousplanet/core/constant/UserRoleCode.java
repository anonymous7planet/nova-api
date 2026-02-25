package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum UserRoleCode implements BaseEnum<String> {

    SYSTEM("ROLE_SYSTEM", "시스템 관리자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    MANAGER("ROLE_MANAGER", "매니저"),
    USER("ROLE_USER", "사용자")
    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static UserRoleCode fromCode(String code) {
        return EnumUtils.fromCode(UserRoleCode.class, code);
    }
}
