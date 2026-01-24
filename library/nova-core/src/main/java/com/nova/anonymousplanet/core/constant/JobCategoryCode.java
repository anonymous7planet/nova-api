package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;

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

    JobCategoryCode(final String code, final String desc) {
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
    public static JobCategoryCode fromCode(final String code) {
        return EnumUtils.fromCode(JobCategoryCode.class, code);
    }

    @Converter(autoApply = true)
    public static class JobCategoryCodeConverter extends BaseEnumConverter<JobCategoryCode, String> {
        public JobCategoryCodeConverter() {
            super(JobCategoryCode.class);
        }
    }
}
