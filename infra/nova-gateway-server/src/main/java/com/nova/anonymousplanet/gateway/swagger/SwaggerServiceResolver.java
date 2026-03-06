package com.nova.anonymousplanet.gateway.swagger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.swagger
 * fileName : SwaggerServiceResolver
 * author : Jinhong Min
 * date : 2026-03-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-06      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class SwaggerServiceResolver {

    private final DiscoveryClient discoveryClient;

    /**
     * Swagger UI에 노출할 서비스 목록 생성
     */
    public Map<String, Object> getSwaggerUrls() {
        List<String> services = discoveryClient.getServices();
        List<Map<String, String>> urls = new ArrayList<>();

        // 1. Swagger UI 규격에 맞는 'urls' 배열 생성
        services.stream()
                // Project Nova 규칙: '-service'로 끝나는 마이크로서비스만 명세에 포함
                .filter(this::isApiService)
                .forEach(serviceId -> {
                    Map<String, String> urlMap = new LinkedHashMap<>();
                    urlMap.put("name", serviceId.toUpperCase());
                    urlMap.put("url", "/" + serviceId.toLowerCase() + "/v3/api-docs");
                    urls.add(urlMap);
                });

        // 2. 핵심 해결책: 서비스가 하나도 없을 때 에러 방지용 더미 데이터 주입
        if (urls.isEmpty()) {
            Map<String, String> dummy = new LinkedHashMap<>();
            dummy.put("name", "No Service Available");
            dummy.put("url", "/v3/api-docs"); // 존재하지 않아도 구조상 필요
            urls.add(dummy);
        }

        // 3. Swagger UI가 요구하는 최종 JSON 포맷 조립
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("urls", urls);
        config.put("configUrl", "/swagger-dynamic");
        config.put("validatorUrl", ""); // 외부 검증기 차단
        // 부가 옵션 (UI 편의성 증대)
        config.put("deepLinking", true);
        config.put("displayOperationId", true);

        // 4. UI가 터지는 것을 방지하기 위한 기본값 강제 지정
        config.put("primaryName", urls.get(0).get("name"));

        log.info("[Swagger] 실시간 갱신 완료. 등록된 서비스 수: {}", urls.size());
        log.info("[Swagger] 갱신된 서비스 목록: {}", urls);

        return config;
    }

    /**
     * Swagger에 노출할 서비스만 필터링
     */
    private boolean isApiService(String serviceId) {
        return serviceId.toLowerCase().startsWith("nova-") && serviceId.toLowerCase().endsWith("-service");
    }

}
