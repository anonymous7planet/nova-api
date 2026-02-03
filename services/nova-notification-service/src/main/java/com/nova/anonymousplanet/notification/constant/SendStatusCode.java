package com.nova.anonymousplanet.notification.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.constant
 * fileName : SendStatus
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */


@Getter
public enum SendStatusCode {
    SUCCESS("S", "성공")
    , FAIL("F", "실패")
    ;

    private final String code;
    private final String desc;

    SendStatusCode(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Converter
    public static class SendStatusCodeConverter implements AttributeConverter<SendStatusCode, String> {
        @Override
        public String convertToDatabaseColumn(SendStatusCode sendStatusCode) {
            return sendStatusCode != null ? sendStatusCode.getCode() : null;
        }

        @Override
        public SendStatusCode convertToEntityAttribute(String s) {
            if(s == null) {
                return null;
            }
            for(SendStatusCode sendStatusCode : SendStatusCode.values()){
                if(sendStatusCode.getCode().equals(s)) {
                    return sendStatusCode;
                }
            }
            return null;
        }
    }

}
