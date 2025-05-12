package com.nova.anonymousplanet.persistence.common;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.entity.common
  fileName : BaseEntity
  author : Jinhong Min
  date : 2025-05-02
  description :
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-05-02      Jinhong Min      최초 생성
  ==============================================
 */
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    protected Long createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    protected Long updatedBy;
}
