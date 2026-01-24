package com.nova.anonymousplanet.gateway.configuration.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration.properties
 * fileName : JwtAuthProperties
 * author : Jinhong Min
 * date : 2026-01-20
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-20      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt.auth")
@Getter
@Setter
public class JwtAuthProperties {
    /**
     * <pre>
     * JWT 인증 검증을 제외할 API 경로 목록입니다.
     * AntPathMatcher 패턴을 지원하며(예: /v1/code/**),
     * 이 목록에 포함된 경로는 JwtAuthenticationGatewayFilter에서 인증 로직을 건너뜁니다.
     * </pre>
     */
    private List<String> excludedPaths = new ArrayList<>();

    @PostConstruct
    public void init() {
        log.info(">>>> [JwtAuthProperties 주입 확인] excludedPaths: {}", excludedPaths);
    }
}
