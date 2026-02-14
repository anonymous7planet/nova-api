package com.nova.anonymousplanet.core.constant.error;

import com.nova.anonymousplanet.core.constant.BaseEnum;

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
    // --- Technical (Status Based) ---
    BAD_REQUEST("BR", "400: 잘못된 요청"),
    VALIDATION("VAL", "400: 유효성 검증 실패"),
    UNAUTHORIZED("ATH", "401: 인증 실패"),
    FORBIDDEN("FOR", "403: 권한 부족"),
    NOT_FOUND("NF", "404: 리소스 없음"),
    CONFLICT("CON", "409: 상태 충돌/중복"),
    SERVER_ERROR("SYS", "500: 내부 장애"),
    EXTERNAL("EXT", "외부 연동 오류"),

    // --- Business (Domain Based) ---
    USER("USER", "회원 관리"),
    MATCH("MATCH", "매칭/소개"),
    CHAT("CHAT", "메시지/채팅"),
    POINT("POINT", "결제/포인트"),
    AD("AD", "광고/프로모션"),
    COUPON("CPN", "쿠폰"),
    NOTICE("NTC", "공지사항/알림")

    ;

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
