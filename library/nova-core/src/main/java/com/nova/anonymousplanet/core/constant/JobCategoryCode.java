package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : JobCateogryCode
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
public enum JobCategoryCode implements BaseEnum<String> {

    OFFICE("OFFICE", "일반 직장인"),
    PROFESSIONAL("PROFESSIONAL", "전문직"),
    PUBLIC("PUBLIC", "공무원/공공기관"),
    BUSINESS("BUSINESS", "사업가"),
    FREELANCER("FREELANCER", "프리랜서"),
    STUDENT("STUDENT", "학생"),
    ETC("ETC", "기타");
    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static JobCategoryCode fromCode(final String code) {
        return EnumUtils.fromCode(JobCategoryCode.class, code);
    }
}
