package com.nova.anonymousplanet.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nova.anonymousplanet.core.configuration.BaseEnumConverter;
import com.nova.anonymousplanet.core.util.EnumUtils;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : NotificationTypeCode
 * author : Jinhong Min
 * date : 2026-02-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-04      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
@RequiredArgsConstructor
public enum NotificationTypeCode implements BaseEnum<String> {

    EMAIL("EMAIL", "이메일"),
    PUSH("PUSH", "푸쉬"),
    SMS("SMS", "문자메세지")

    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static NotificationTypeCode fromCode(String code) {
        return EnumUtils.fromCode(NotificationTypeCode.class, code);
    }

    @Converter
    public static class NotificationTypeCodeConverter extends BaseEnumConverter<NotificationTypeCode, String> {
        public NotificationTypeCodeConverter() {
            super(NotificationTypeCode.class);
        }
    }
}
