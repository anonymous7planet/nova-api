package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.gateway.constant.LogHeaderCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : ClientInfoFliter
 * author : Jinhong Min
 * date : 2025-12-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-02      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
public class ClientInfoFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return FilterOrder.CLIENT_INFO; // 5번
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // X-Forwarded-For 우선, 없으면 remote address 사용
        String clientIp = request.getHeaders().getFirst("X-Forwarded-For");
        if (clientIp == null || clientIp.isBlank()) {
            if (request.getRemoteAddress() != null && request.getRemoteAddress().getAddress() != null) {
                clientIp = request.getRemoteAddress().getAddress().getHostAddress();
            } else {
                clientIp = "unknown";
            }
        } else {
            clientIp = clientIp.split(",")[0].trim();
        }

        String ua = request.getHeaders().getFirst("User-Agent");
        String deviceType = request.getHeaders().getFirst(LogHeaderCode.DEVICE_TYPE.getKey());
        String os = request.getHeaders().getFirst(LogHeaderCode.OS_TYPE.getKey());
        String osVersion = request.getHeaders().getFirst(LogHeaderCode.OS_VERSION.getKey());
        String appVersion = request.getHeaders().getFirst(LogHeaderCode.APP_VERSION.getKey());

        log.debug("[ClientInfoFilter] clientIp={}, ua={}, deviceType={}, os={}, osVersion={}, appVersion={}",
                clientIp, ua, deviceType, os, osVersion, appVersion);

        ServerHttpRequest mutated = request.mutate()
                .header(LogHeaderCode.CLIENT_IP.getKey(), clientIp)
                .header(LogHeaderCode.USER_AGENT.getKey(), ua != null ? ua : "")
                // 이미 프론트에서 넘겼다면 재사용(여기선 그냥 전달)
                .header(LogHeaderCode.DEVICE_TYPE.getKey(), deviceType != null ? deviceType : "")
                .header(LogHeaderCode.OS_TYPE.getKey(), os != null ? os : "")
                .header(LogHeaderCode.OS_VERSION.getKey(), osVersion != null ? osVersion : "")
                .header(LogHeaderCode.APP_VERSION.getKey(), appVersion != null ? appVersion : "")
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }
}
