package com.nova.anonymousplanet.gateway.configuration.properties;

import com.nova.anonymousplanet.core.constant.SecurityConstants;
import com.nova.anonymousplanet.core.util.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;
import java.util.stream.Stream;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration.properties
 * fileName : JwtAuthProperties
 * author : Jinhong Min
 * date : 2026-01-20
 * description :
 * JWT 인증 검증을 제외할 API 경로 목록입니다.
 * AntPathMatcher 패턴을 지원하며(예: /v1/code/**),
 * 이 목록에 포함된 경로는 JwtAuthenticationGatewayFilter에서 인증 로직을 건너뜁니다.
 * Project Nova: 게이트웨이 전용 보안 설정 속성 (Record)
 * @param freePaths YAML에서 주입받는 유동 경로 (nova.security.free-paths)
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-20      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
@ConfigurationProperties(prefix = "nova.security")
public record NovaGatewaySecurityProperties(
        @DefaultValue List<String> freePaths
) {
    // 1. 게이트웨이 전용 고정 경로 (인증 서비스 관련 핵심 경로)
    private static final List<String> GATEWAY_STATIC_FREE_PATHS = List.of(
            "/v1/signup",
            "/v1/login",
            "/v1/token/refresh"
    );

    /**
     * 컴팩트 생성자: 초기화 로직 및 로그 출력
     */
    public NovaGatewaySecurityProperties {
        if (freePaths == null) {
            freePaths = List.of();
        }
        // record는 @PostConstruct를 못 쓰므로 생성 시점에 로그를 찍습니다.
        log.info(">>>> [NovaGatewaySecurityProperties] Initialized with freePaths: {}", freePaths);
    }

    /**
     * 전체 무료 통과 경로 리스트 통합 반환
     */
    public List<String> getFinalFreePaths() {
        return Stream.of(
                        SecurityConstants.SYSTEM_FREE_PATH_PATTERNS.stream(),      // 1. 전사 공통 (Constants)
                        GATEWAY_STATIC_FREE_PATHS.stream(),                        // 2. 게이트웨이 고정 (Static)
                        freePaths.stream()                                         // 3. 운영 유동 (YAML)
                )
                .flatMap(s -> s)
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
