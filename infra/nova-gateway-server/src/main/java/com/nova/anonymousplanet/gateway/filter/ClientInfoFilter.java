package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.gateway.constant.LogContextCode;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import com.nova.anonymousplanet.gateway.util.ClientUtils;
import com.nova.anonymousplanet.gateway.util.LogContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;

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
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 게이트웨이가 보존하고 있는 원본 URI 세트를 가져옴
        Set<URI> originalUris = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

        String originalUrl;
        if (originalUris != null && !originalUris.isEmpty()) {
            // 첫 번째 값이 실제 클라이언트가 요청한 최초의 원본 URL입니다.
            originalUrl = originalUris.iterator().next().getPath();
        } else {
            // 혹시 속성이 비어있다면 현재 요청의 경로를 사용
            originalUrl = exchange.getRequest().getURI().getPath();
        }

        ServerHttpRequest request = exchange.getRequest();

        // X-Forwarded-For 우선, 없으면 remote address 사용
        String clientIp = ClientUtils.getClientIp(request);

        log.info("[ClientInfoFilter] {}", clientIp);

        ServerHttpRequest mutated = request.mutate()
                .header(LogContextCode.CLIENT_IP.getHeaderKey(), clientIp)
                .header(LogContextCode.REQUEST_PATH.getHeaderKey(), originalUrl)
                .build();

        return chain.filter(exchange.mutate().request(mutated).build())
                .contextWrite(LogContextUtils.populateContext(mutated));
    }

    @Override
    public int getOrder() {
        return FilterOrder.CLIENT_INFO; // 5번
    }
}
