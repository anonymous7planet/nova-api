package com.nova.anonymousplanet.common.constant.error;

import com.nova.anonymousplanet.common.constant.BaseEnum;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.constant
  fileName : ErrorGroupCode
  author : Jinhong Min
  date : 2025-10-31
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-31      Jinhong Min      최초 생성
  ==============================================
 */
public enum ErrorGroupCode implements BaseEnum<String> {
    COMMON("A000", "공통 오류"),
    VALIDATION("A002", "유효성 검증 오류"),
    AUTH("B000", "인증 관련 오류"),
    TOKEN("C001", "JWT/토큰 관련 오류"),
    ACCESS("C000", "권한 관련 오류"),
    RESOURCE("D000", "자원 관련 오류"),
    USER("U000", "회원 관련 오류"),
    SERVER("S000", "서버 내부 오류");

    private final String code;
    private final String desc;

    ErrorGroupCode(final String code, final String desc) {
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
}
