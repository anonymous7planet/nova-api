package com.nova.anonymousplanet.auth.configuration;

import com.nova.anonymousplanet.security.context.UserContext;
import com.nova.anonymousplanet.security.context.UserInfo;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.configuration.exception
  fileName : AuditorAwareImpl
  author : Jinhong Min
  date : 2025-11-06
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-06      Jinhong Min      최초 생성
  ==============================================
 */
public class AuditorAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        UserInfo context = UserContext.getUserInfo();
        return Optional.ofNullable(context != null ? context.userId() : null);
    }
}
