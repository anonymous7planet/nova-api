package com.nova.anonymousplanet.messaging.model;


import com.nova.anonymousplanet.messaging.constant.EmailTemplateTypeCode;

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
    EmailTemplateTypeCode templateType,
    Map<String, Object> variables,
    List<EmailAttachment> attachments,
    List<InlineImage> inlineImages
) {
}
