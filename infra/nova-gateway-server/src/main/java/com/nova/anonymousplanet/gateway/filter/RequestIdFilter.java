package com.nova.anonymousplanet.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : RequestIdFilter
 * author : Jinhong Min
 * date : 2025-11-15
 * description :
 * [Order 0] 모든 요청에 대해 고유한 요청 ID (X-Request-ID)를 생성하고 헤더에 주입합니다.
 * 분산 트레이싱 및 로그 상관관계를 위한 필수 필터입니다.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-15      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
@Component
public class RequestIdFilter implements GlobalFilter, Ordered {

    private static final String HEADER_NAME = "X-Request-ID";
    private static final String CORRELATION_HEADER = "X-Correlation-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        // 1. 기존 ID가 없으면 UUID 생성
        String requestId = headers.getFirst(HEADER_NAME);
        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        // 2. Correlation ID 설정 (없으면 Request ID와 동일하게 설정)
        String correlationId = headers.getFirst(CORRELATION_HEADER);
        if (!StringUtils.hasText(correlationId)) {
            correlationId = requestId;
        }

        // 3. 변조된 요청 객체(Mutated Request)에 ID 헤더 추가
        ServerHttpRequest mutated = request.mutate()
            .header(HEADER_NAME, requestId)
            .header(CORRELATION_HEADER, correlationId)
            .build();

        log.debug("Assigned RequestId={}, CorrelationId={} for {} {}",
            requestId, correlationId, request.getMethod().name(), request.getURI());

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    @Override
    public int getOrder() {
        return FilterOrder.REQUEST_ID;
    }
}
