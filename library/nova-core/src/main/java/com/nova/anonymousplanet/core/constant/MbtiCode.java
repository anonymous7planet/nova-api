package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.common.constant
 * fileName : MbtiCode
 * author : Jinhong Min
 * date : 2025-04-30
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-30      Jinhong Min      최초 생성
 * ==============================================
 */

@Getter
@RequiredArgsConstructor
public enum MbtiCode implements BaseEnum<String> {

    ISTJ("ISTJ", "ISTJ"),
    ISFJ("ISFJ", "ISFJ"),
    INFJ("INFJ", "INFJ"),
    INTJ("INTJ", "INTJ"),
    ISTP("ISTP", "ISTP"),
    ISFP("ISFP", "ISFP"),
    INFP("INFP", "INFP"),
    INTP("INTP", "INTP"),
    ESTP("ESTP", "ESTP"),
    ESFP("ESFP", "ESFP"),
    ENFP("ENFP", "ENFP"),
    ENTP("ENTP", "ENTP"),
    ESTJ("ESTJ", "ESTJ"),
    ESFJ("ESFJ", "ESFJ"),
    ENFJ("ENFJ", "ENFJ"),
    ENTJ("ENTJ", "ENTJ"),
    NONE("NONE", "NONE")        // MBTI를 모를 경우
    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static MbtiCode fromCode(String code) {
        return EnumUtils.fromCode(MbtiCode.class, code);
    }

}
