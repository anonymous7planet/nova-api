package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.constant
  fileName : AdminRoleCode
  author : Jinhong Min
  date : 2025-09-29
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-09-29      Jinhong Min      최초 생성
  ==============================================
 */
@Getter
@RequiredArgsConstructor
public enum AdminRoleCode implements BaseEnum<String> {

    SYSTEM_ADMIN("ROLE_SYSTEM", "시스템관리자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    MANAGER("ROLE_MANAGER", "매니저"),
    ANALYST("ROLE_ANALYST", "분석가")
    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static AdminRoleCode fromCode(String code) {
        return EnumUtils.fromCode(AdminRoleCode.class, code);
    }
}
