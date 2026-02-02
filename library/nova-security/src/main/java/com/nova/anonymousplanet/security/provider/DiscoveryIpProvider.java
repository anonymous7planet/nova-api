package com.nova.anonymousplanet.security.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.provider
 * fileName : DiscoveryIpProvider
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiscoveryIpProvider {
    private final DiscoveryClient discoveryClient;

    /**
     * 요청을 보낸 IP가 Discovery에 등록된 인스턴스 중 하나인지 확인
     */
    public boolean isTrustedService(String remoteAddr, String serviceId) {
        // 1. 헤더에 서비스 ID가 없다면 신뢰할 수 없음
        if (serviceId == null || serviceId.isBlank()) return false;

        // 2. Eureka에서 해당 서비스 이름으로 등록된 모든 인스턴스 정보를 가져옴
        return discoveryClient.getInstances(serviceId).stream()
                .anyMatch(instance -> instance.getHost().equals(remoteAddr));
    }
}
