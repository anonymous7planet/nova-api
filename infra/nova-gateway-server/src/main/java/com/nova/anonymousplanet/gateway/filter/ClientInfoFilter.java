package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.gateway.constant.LogContextCode;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import com.nova.anonymousplanet.gateway.util.ClientUtils;
import com.nova.anonymousplanet.gateway.util.LogContextUtils;
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
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // X-Forwarded-For 우선, 없으면 remote address 사용
        String clientIp = ClientUtils.getClientIp(request);

        log.info("[ClientInfoFilter] {}", clientIp);

        ServerHttpRequest mutated = request.mutate()
                .header(LogContextCode.CLIENT_IP.getHeaderKey(), clientIp)
                .build();

        return chain.filter(exchange.mutate().request(mutated).build())
                .contextWrite(LogContextUtils.populateContext(mutated));
    }

    @Override
    public int getOrder() {
        return FilterOrder.CLIENT_INFO; // 5번
    }
}
