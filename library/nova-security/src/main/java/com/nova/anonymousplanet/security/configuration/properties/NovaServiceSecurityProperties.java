package com.nova.anonymousplanet.security.configuration.properties;

import com.nova.anonymousplanet.core.constant.SecurityConstants;
import com.nova.anonymousplanet.core.util.PathUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.configuration
 * fileName : NovaSecurityProperties
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */


@ConfigurationProperties(prefix = "nova.security")
public record NovaServiceSecurityProperties(
        String xGatewaySecret,
        @DefaultValue List<String> freePaths
){
    public NovaServiceSecurityProperties {
        if (freePaths == null) {
            freePaths = List.of();
        }
    }

    /**
     * 전사 공통 경로(SecurityConstants)와 서비스 전용 경로(freePaths)를 통합하여 반환
     */
    public List<String> getFinalFreePaths() {
        return Stream.concat(
                        SecurityConstants.SYSTEM_FREE_PATH_PATTERNS.stream(),
                        freePaths.stream()
                )
                .distinct()
                .toList();
    }

    /**
     * 요청된 경로가 인증 제외 대상인지 확인
     * @param path 요청 URL 경로
     * @return 제외 여부 (true: 통과, false: 인증 필요)
     */
    public boolean isFreePath(String path) {
        return getFinalFreePaths().stream()
                .anyMatch(pattern -> PathUtils.match(pattern, path));
    }
}
