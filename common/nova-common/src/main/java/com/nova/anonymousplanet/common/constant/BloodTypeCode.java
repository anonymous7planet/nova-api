package com.nova.anonymousplanet.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.common.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.common.util.EnumUtils;

import javax.persistence.Converter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.common.constant
 * fileName : BloodTypeCode
 * author : Jinhong Min
 * date : 2025-04-30
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-30      Jinhong Min      최초 생성
 * ==============================================
 */
public enum BloodTypeCode implements BaseEnum<String> {

    A("A", "A형"),
    B("B", "B형"),
    O("O", "O형"),
    AB("AB", "AB형")
    ;

    private final String code;
    private final String desc;

    BloodTypeCode(final String code, final String desc) {
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
    public BloodTypeCode fromCode(String code) {
        return EnumUtils.fromCode(BloodTypeCode.class, code);
    }

    @Converter(autoApply = false)
    public static class BloodTypeCodeConverter extends BaseEnumConverter<BloodTypeCode, String> {
        public BloodTypeCodeConverter() {
            super(BloodTypeCode.class);
        }
    }
}
