package com.nova.anonymousplanet.messaging.constant;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant
 * fileName : EmailTemplateTypeCode
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
public enum EmailTemplateTypeCode {

    WELCOME("welcome", "%s님 단비 회원 가입을 축하합니다", "회원 가입 환영 메일")
    , PASSWORD_RESET("password-reset", "비밀번호 재설정을 해주세요", "비밀번호 재설정 메일")
    , VERIFY_CODE("verify-code", "인증번호 발송", "회원가입시 인증번호")
    , ACCOUNT_WITHDRAW("account-withdraw", "%s님 단비 회원 탈퇴 안내", "회원 탈퇴 안내 메시지")
    ;

    private final String fileName;
    private final String title; // 메일 제목
    private final String desc;

    EmailTemplateTypeCode(String fileName, String title, String desc) {
        this.fileName = fileName;
        this.title = title;
        this.desc = desc;
    }



    @Converter
    public static class EmailTemplateTypeCodeConverter implements AttributeConverter<EmailTemplateTypeCode, String> {
        @Override
        public String convertToDatabaseColumn(EmailTemplateTypeCode emailTemplateTypeCode) {
            return emailTemplateTypeCode != null ? emailTemplateTypeCode.getFileName() : null;
        }

        @Override
        public EmailTemplateTypeCode convertToEntityAttribute(String s) {
            if(s == null) {
                return null;
            }
            for(EmailTemplateTypeCode type : EmailTemplateTypeCode.values()) {
                if(type.getFileName().equals(s)) {
                    return type;
                }
            }
            return null;
        }
    }




}