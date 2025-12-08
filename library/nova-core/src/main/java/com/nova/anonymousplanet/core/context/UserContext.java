package com.nova.anonymousplanet.core.context;

import com.nova.anonymousplanet.core.constant.UserRoleCode;
import com.nova.anonymousplanet.core.constant.UserStatusCode;

import java.io.Serializable;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.common.dto.common
  fileName : UserContextInfo
  author : Jinhong Min
  date : 2025-11-06
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-06      Jinhong Min      최초 생성
  ==============================================
 */
public record UserContext(
    Long userId,
    String userUuid,
    UserRoleCode userRole,
    UserStatusCode userStatus,
    String ipAddress
) implements Serializable {
}
