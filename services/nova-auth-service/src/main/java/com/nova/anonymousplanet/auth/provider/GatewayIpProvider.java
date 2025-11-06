package com.nova.anonymousplanet.auth.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.provider
  fileName : GatewayIPProvider
  author : Jinhong Min
  date : 2025-11-04
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-11-04      Jinhong Min      최초 생성
  ==============================================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayIpProvider {
    private final DiscoveryClient discoveryClient;

    private final Set<String> gatewayIps = new HashSet<>();

    /**
     * Eureka에 등록된 gateway 인스턴스들의 IP 목록을 주기적으로 갱신
     */
    @Scheduled(fixedDelay = 30000) // 30초마다 갱신
    public void refreshGatewayIps() {
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("nova-gateway-server");
            Set<String> newIps = new HashSet<>();

            for (ServiceInstance instance : instances) {
                newIps.add(instance.getHost());
            }

            gatewayIps.clear();
            gatewayIps.addAll(newIps);

            log.debug("Updated Gateway IPs: {}", gatewayIps);
        } catch (Exception e) {
            log.error("Failed to refresh gateway IP list from discovery: {}", e.getMessage());
        }
    }

    public boolean isGatewayIp(String ip) {
        return gatewayIps.contains(ip);
    }
}
