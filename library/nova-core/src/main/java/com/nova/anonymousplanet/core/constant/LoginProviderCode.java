package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : LoginProvider
 * author : Jinhong Min
 * date : 2026-01-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-06      Jinhong Min      최초 생성
 * ==============================================
 */
public enum LoginProviderCode implements BaseEnum<String>{

    DANBY("DANBY", "DANBY를 통해 회원가입"),
    KAKAO("KAKAO", "KAKAO를 통해 회원가입"),
    NAVER("NAVER", "NAVER를 통해 회원가입"),
    APPLE("APPLE" , "APPLE을 통해 회원가입")
    ;


    private final String code;
    private final String desc;


    LoginProviderCode(final String code, final String desc) {
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
    public static LoginProviderCode LoginProviderCode(String code) {
        return EnumUtils.fromCode(LoginProviderCode.class, code);
    }

    @Converter
    public static class LoginProviderCodeConverter extends BaseEnumConverter<LoginProviderCode, String> {
        protected LoginProviderCodeConverter() {
            super(LoginProviderCode.class);
        }
    }
}
