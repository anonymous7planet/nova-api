package com.nova.anonymousplanet.messaging.constant;

import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.constant
 * fileName : SendStatus
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */


@Getter
public enum SendStatusCode {
    SUCCESS("S", "성공")
    , FAIL("F", "실패")
    ;

    private final String code;
    private final String desc;

    SendStatusCode(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}
