package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : UserTypeCode
 * author : Jinhong Min
 * date : 2025-11-18
 * description : 
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-18      Jinhong Min      최초 생성
 * ==============================================
 */
public enum UserTypeCode implements BaseEnum<String>{
    USER("USER", "일반회원")
    , ADMIN("ADMIN", "관리자")
    ;

    private final String code;
    private final String desc;

    UserTypeCode(final String code, final String desc) {
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
    public static UserTypeCode fromCode(String code) {
        return EnumUtils.fromCode(UserTypeCode.class, code);
    }
}
