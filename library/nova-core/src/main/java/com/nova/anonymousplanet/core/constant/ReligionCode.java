package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : ReligionCode
 * author : Jinhong Min
 * date : 2026-01-07
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-07      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
@RequiredArgsConstructor
public enum ReligionCode implements BaseEnum<String>{
    NONE("NONE", "무교"),
    CHRISTIAN("CHRISTIAN", "기독교"),
    BUDDHIST("BUDDHIST", "불교"),
    CATHOLIC("CATHOLIC", "천주교"),
    ETC("ETC", "기타")
    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static ReligionCode fromCode(final String code) {
        return EnumUtils.fromCode(ReligionCode.class, code);
    }
}
