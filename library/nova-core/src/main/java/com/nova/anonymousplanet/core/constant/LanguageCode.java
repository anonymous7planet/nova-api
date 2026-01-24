package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;


/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.constant
  fileName : LanguageCode
  author : Jinhong Min
  date : 2025-10-14
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-14      Jinhong Min      최초 생성
  ==============================================
 */
public enum LanguageCode implements BaseEnum<String> {

    KOREAN("KO", "한국어"),
    ENGLISH("EN", "영어"),
    JAPANESE("JA", "일본어"),
    CHINES("CH", "중국어")
    ;

    private final String code;
    private final String desc;

    LanguageCode(final String code, final String desc) {
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
    public static LanguageCode fromCode(final String code) {
        return EnumUtils.fromCode(LanguageCode.class, code);
    }

    @Converter(autoApply = true)
    public static class LanguageCodeConverter extends BaseEnumConverter<LanguageCode, String> {
        public LanguageCodeConverter() {
            super(LanguageCode.class);
        }
    }
}
