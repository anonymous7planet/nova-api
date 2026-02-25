package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


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

@Getter
@RequiredArgsConstructor
public enum LanguageCode implements BaseEnum<String> {

    KOREAN("KO", "한국어"),
    ENGLISH("EN", "영어"),
    JAPANESE("JA", "일본어"),
    CHINES("CH", "중국어")
    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static LanguageCode fromCode(final String code) {
        return EnumUtils.fromCode(LanguageCode.class, code);
    }
}
