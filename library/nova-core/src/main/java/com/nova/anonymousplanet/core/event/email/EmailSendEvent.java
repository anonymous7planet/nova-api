package com.nova.anonymousplanet.core.event.email;

import java.util.List;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.dto.v1
 * fileName : EmailSendEvent
 * author : Jinhong Min
 * date : 2026-02-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-06      Jinhong Min      최초 생성
 * ==============================================
 */
public record EmailSendEvent(
        Long recipientId,
        String email,
        String templateCode,
        Map<String, Object> variables,
        List<EmailAttachment> attachments,
        List<InlineImage> inlineImages
) {}
