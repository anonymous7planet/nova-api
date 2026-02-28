package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : DisplayTypeCode
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
@RequiredArgsConstructor
public enum DisplayTypeCode implements BaseEnum<String> {

    TOAST("TOAST", "토스트 메시지"),
    ALERT("ALERT", "경고 메시지"),
    CONFIRM("CONFIRM","확인 메시지"),
    LOG("LOG", "로그"),
    PAGE("PAGE", "페이지로 이동"),
    NONE("NONE", "표시하지않음")
    ;

    private final String code;
    private final String desc;


    @JsonValue
    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
