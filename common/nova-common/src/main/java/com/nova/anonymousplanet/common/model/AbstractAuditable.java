package com.nova.anonymousplanet.common.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.common.model
 * fileName : AbstractAuditable
 * author : Jinhong Min
 * date : 2025-04-28
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-28      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
public abstract class AbstractAuditable {
    protected LocalDateTime createdAt;
    protected String createdBy;
    protected LocalDateTime updatedAt;
    protected String updatedBy;
}
