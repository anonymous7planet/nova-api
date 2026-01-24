package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;
import org.aspectj.apache.bcel.classfile.Code;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : ServiceModeCode
 * author : Jinhong Min
 * date : 2026-01-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-06      Jinhong Min      최초 생성
 * ==============================================
 */
public enum ServiceModeCode implements BaseEnum<String> {
    NONE("NONE", "미승인 유저"),
    FRIEND("FRIEND", "친구 모드(승인완료)"),
    MATCH_PENDING("MATCH_PENDING", "맞선 모드 승인 대기"),
    MATCH("MATCH", "맞선 모드(최종 승인완료)");

    private final String code;
    private final String desc;

    ServiceModeCode(final String code, final String desc) {
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
    public static ServiceModeCode fromCode(String code) {
        return EnumUtils.fromCode(ServiceModeCode.class, code);
    }

    @Converter
    public static class ServiceModeCodeConverter extends BaseEnumConverter<ServiceModeCode, String> {
        public ServiceModeCodeConverter() {
            super(ServiceModeCode.class);
        }
    }
}
