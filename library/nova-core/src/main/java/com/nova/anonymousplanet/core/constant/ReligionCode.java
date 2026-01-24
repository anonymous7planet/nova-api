package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;
import org.aspectj.apache.bcel.classfile.Code;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : ReligionCode
 * author : Jinhong Min
 * date : 2026-01-07
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-07      Jinhong Min      최초 생성
 * ==============================================
 */
public enum ReligionCode implements BaseEnum<String>{
    NONE("NONE", "무교"),
    CHRISTIAN("CHRISTIAN", "기독교"),
    BUDDHIST("BUDDHIST", "불교"),
    CATHOLIC("CATHOLIC", "천주교"),
    ETC("ETC", "기타")
    ;

    private final String code;
    private final String desc;

    ReligionCode(final String code, final String desc) {
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
    public static ReligionCode fromCode(final String code) {
        return EnumUtils.fromCode(ReligionCode.class, code);
    }

    @Converter(autoApply = true)
    public static class ReligionCodeConverter extends BaseEnumConverter<ReligionCode, String> {
        public ReligionCodeConverter() {
            super(ReligionCode.class);
        }
    }
}
