package com.nova.anonymousplanet.auth.dto;

import com.nova.anonymousplanet.core.constant.RoleCode;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.dto
  fileName : TokenDto
  author : Jinhong Min
  date : 2025-11-04
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-04      Jinhong Min      최초 생성
  ==============================================
 */
public record TokenDto() {

    public record IssueRequest(
        String userUuid,
        RoleCode role,
        String deviceId
    ) {
    }

    public record IssueResponse(
        String token,
        Long expiresIn
    ) {
    }
}
