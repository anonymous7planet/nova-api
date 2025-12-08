package com.nova.anonymousplanet.messaging.entity;

import com.nova.anonymousplanet.messaging.constant.EmailTemplateTypeCode;
import com.nova.anonymousplanet.messaging.constant.SendStatusCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.entity
 * fileName : EmailLogEntity
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */

@Entity
@Table(name = "tb_email_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class EmailLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender", length = 100, nullable = false)
    private String sender;

    @Column(name = "receiver", length = 100, nullable = false)
    private String receiver;

    @Column(name = "subject", length = 100, nullable = false)
    private String subject;

    @Convert(converter = SendStatusCode.SendStatusCodeConverter.class)
    @Column(name = "status", length = 1, nullable = false)
    private SendStatusCode status;

    @Column(name = "error_message", length = 300, nullable = false)
    private String errorMessage;

    @Convert(converter = EmailTemplateTypeCode.EmailTemplateTypeCodeConverter.class)
    @Column(name = "user_template", length = 50, nullable = false)
    private EmailTemplateTypeCode userTemplate;

    @CreatedDate
    @Column(name = "send_at", updatable = false)
    private LocalDateTime sendAt;
}
