package com.nova.anonymousplanet.messaging.entity;

import com.nova.anonymousplanet.messaging.constant.SendStatusCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
@Setter
public class EmailLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver;
    private String subject;

    @Enumerated(EnumType.STRING)
    private SendStatusCode status;

    private String errorMessage;

    private LocalDateTime sendAt = LocalDateTime.now();
}
