package com.nova.anonymousplanet.system.configuration;

import com.nova.anonymousplanet.security.context.UserContext;
import com.nova.anonymousplanet.security.context.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.configuration
 * fileName : JpaAuditConfiguration
 * author : Jinhong Min
 * date : 2026-02-03
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-03      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
@EnableJpaAuditing // JPA Auditing 활성화 필수!
public class JpaAuditConfiguration {
    /**
     * [Project Nova] AuditorAware 빈 등록
     * 작성자/수정자 ID를 UserContext에서 추출하여 자동으로 채워줍니다.
     */
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            UserInfo userInfo = UserContext.getUserInfo();
            return Optional.ofNullable(userInfo)
                    .map(UserInfo::userId); // Long ID 반환
        };
    }
}
