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

import java.util.UUID;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : TraceRequestIdFilter
 * author : Jinhong Min
 * date : 2025-12-02
 * description :
 * TraceRequestFilter
 * - X-Trace-Id, X-Request-Id 를 생성 및 주입
 * - 이미 헤더가 존재하면 재사용, 없으면 신규 발급
 * - MDC에 넣는 작업은 Gateway에서는 하지 않음(reactive 환경, 필요 시 ReactorContext 사용)
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-02      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
public class TraceIdFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return FilterOrder.TRACE_ID;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String traceId = request.getHeaders().getFirst(LogHeaderCode.TRACE_ID.getKey());
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
            log.debug("[TraceRequestFilter] Generated new TraceId={}", traceId);
        } else {
            log.debug("[TraceRequestFilter] Reusing TraceId={}", traceId);
        }

        ServerHttpRequest mutated = request.mutate()
                .header(LogHeaderCode.TRACE_ID.getKey(), traceId)
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }
}
