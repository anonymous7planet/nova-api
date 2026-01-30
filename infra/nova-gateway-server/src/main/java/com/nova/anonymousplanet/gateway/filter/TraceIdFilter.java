package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.gateway.constant.LogContextCode;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import com.nova.anonymousplanet.gateway.util.LogContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

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
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // 1. TraceId 추출 또는 생성
        String traceId = request.getHeaders().getFirst(LogContextCode.TRACE_ID.getHeaderKey());
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
            log.debug("[TraceIdFilter] Generated new TraceId={}", traceId);
        } else {
            log.debug("[TraceFilter] Reusing TraceId={}", traceId);
        }

        // 2. [중요] Exchange Attributes에 저장하여 Gateway 내부 전역에서 공유
        exchange.getAttributes().put(LogContextCode.TRACE_ID.getMdcKey(), traceId);

        // 3. 내부 서비스 전파를 위한 Mutated Request 생성
        ServerHttpRequest mutated = request.mutate()
                .header(LogContextCode.TRACE_ID.getHeaderKey(), traceId)
                .build();

        // 4. 컨텍스트 전파 및 체인 실행
        return chain.filter(exchange.mutate().request(mutated).build())
                .contextWrite(LogContextUtils.populateContext(mutated));
    }

    @Override
    public int getOrder() {
        return FilterOrder.TRACE_ID;
    }
}
