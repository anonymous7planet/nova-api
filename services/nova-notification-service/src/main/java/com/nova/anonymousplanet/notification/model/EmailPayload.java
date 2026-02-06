package com.nova.anonymousplanet.notification.model;



import com.nova.anonymousplanet.core.event.email.EmailAttachment;
import com.nova.anonymousplanet.core.event.email.InlineImage;
import com.nova.anonymousplanet.notification.domain.entity.EmailHistoryEntity;

import java.util.List;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.dto.sender
 * fileName : EmailPayloadDto
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */

public record EmailPayload(
        String to,
        String subject,
        String templateFileName, // Enum이 아닌 실제 파일명 스트링
        Map<String, Object> variables,
        List<EmailAttachment> attachments,
        List<InlineImage> inlineImages
) {
    // 팩토리 메서드: 일반적인 템플릿 기반 메일 생성 시 사용
    public static EmailPayload of(String to, String subject, String templateFileName, Map<String, Object> variables) {
        return new EmailPayload(to, subject, templateFileName, variables, null, null);
    }

    /**
     * Entity -> Payload 변환 (정적 팩토리 메서드)
     */
    public static EmailPayload from(EmailHistoryEntity history, List<EmailAttachment> attachments, List<InlineImage> inlineImages) {
        // 엔티티와 연관된 템플릿 정보 추출
        var template = history.getTemplate();

        return new EmailPayload(
                history.getRecipientEmail(),  // 복호화된 수신자 메일
                template.getSubjectPart(),    // 템플릿 제목
                template.getTemplateFileName(),       // HTML 파일명
                history.getVariables(),       // 치환 변수 Map
                attachments,
                inlineImages
        );
    }
}