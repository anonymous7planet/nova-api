package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


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

@Getter
@RequiredArgsConstructor
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

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static BloodTypeCode fromCode(String code) {
        return EnumUtils.fromCode(BloodTypeCode.class, code);
    }
}
