package com.nova.anonymousplanet.persistence.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.persistence.common
  fileName : AbstractAuditable
  author : Jinhong Min
  date : 2025-11-06
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-06      Jinhong Min      최초 생성
  ==============================================
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditable{

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private Long modifiedBy;
}
