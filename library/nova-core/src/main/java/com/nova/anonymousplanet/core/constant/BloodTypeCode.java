package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;


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

    A_POS("A+", "A형 Rh+"),
    A_NEG("A-", "A형 Rh-"),
    B_POS("B+", "B형 Rh+"),
    B_NEG("B-", "B형 Rh-"),
    O_POS("O+", "O형 Rh+"),
    O_NEG("O-", "O형 Rh-"),
    AB_POS("AB+", "AB형 Rh+"),
    AB_NEG("AB-", "AB형 Rh-")
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
    public static BloodTypeCode fromCode(String code) {
        return EnumUtils.fromCode(BloodTypeCode.class, code);
    }

    @Converter(autoApply = true)
    public static class BloodTypeCodeConverter extends BaseEnumConverter<BloodTypeCode, String> {
        public BloodTypeCodeConverter() {
            super(BloodTypeCode.class);
        }
    }
}
