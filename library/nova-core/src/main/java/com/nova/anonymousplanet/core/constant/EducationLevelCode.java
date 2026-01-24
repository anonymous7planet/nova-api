package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : EduCationLevelCode
 * author : Jinhong Min
 * date : 2026-01-07
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-07      Jinhong Min      최초 생성
 * ==============================================
 */
public enum EducationLevelCode implements BaseEnum<String> {
    ELEMENTARY_SCHOOL("ELEMENTARY", "초등학교 졸업"),
    MIDDLE_SCHOOL("MIDDLE", "중학교 졸업"),
    HIGH_SCHOOL("HIGH", "고등학교 졸업"),
    COLLEGE_2Y("COLLEGE", "전문대 졸업"),
    UNIVERSITY_4Y("UNIVERSITY", "대학교 졸업"),
    MASTER("MASTER", "석사 졸업"),
    DOCTOR("DOCTOR", "박사 졸업"),
    ETC("ETC", "기타");
    ;

    private final String code;
    private final String desc;

    EducationLevelCode(final String code, final String desc) {
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
    public static EducationLevelCode fromCode(final String code) {
        return EnumUtils.fromCode(EducationLevelCode.class, code);
    }

    @Converter(autoApply = true)
    public static class EduCationLevelCodeConverter extends BaseEnumConverter<EducationLevelCode, String> {
        public EduCationLevelCodeConverter() {
            super(EducationLevelCode.class);
        }
    }
}
