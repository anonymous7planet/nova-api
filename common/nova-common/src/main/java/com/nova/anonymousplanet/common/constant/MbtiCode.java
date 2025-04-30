package com.nova.anonymousplanet.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.common.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.common.util.EnumUtils;

import javax.persistence.Converter;

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
    ENTJ("ENTJ", "ENTJ")
    ;

    private final String code;
    private final String desc;

    MbtiCode(final String code, final String desc) {
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
    public static MbtiCode fromCode(String code) {
        return EnumUtils.fromCode(MbtiCode.class, code);
    }

    @Converter(autoApply = false)
    public static class MbtiCodeConverter extends BaseEnumConverter<MbtiCode, String> {
        protected MbtiCodeConverter() {
            super(MbtiCode.class);
        }
    }
}
