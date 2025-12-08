package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.gateway.constant.LogHeaderCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : GlobalLogginFilter
 * author : Jinhong Min
 * date : 2025-11-15
 * description :
 * [Order 10] 요청 시작/종료 시점의 로그를 남겨 지연 시간을 측정합니다.
 * RequestIdFilter가 주입한 X-Request-ID를 사용하여 로그를 추적합니다.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-15      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethod().name();
        String path = request.getURI().toString();
        String traceId = exchange.getRequest().getHeaders().getFirst(LogHeaderCode.TRACE_ID.getKey());
        Instant start = Instant.now();

        log.info("[REQ] traceId={} method={} path={}", traceId, method, path);

        return chain.filter(exchange)
            .doOnSuccess(aVoid -> {
                ServerHttpResponse response = exchange.getResponse();
                long durationMs = Duration.between(start, Instant.now()).toMillis();
                // 응답 상태 코드 및 처리 시간을 기록
                log.info("[RES] traceId={} path={} status={} duration={}ms", traceId, path, response.getStatusCode(),
                    durationMs);
            })
            .doOnError(throwable -> {
                long durationMs = Duration.between(start, Instant.now()).toMillis();
                // 에러 발생 시 로그 기록 (주요 예외는 ExceptionHandler가 처리)
                log.warn("[ERR] traceId={} path={} error={} duration={}ms", traceId, path, throwable.getMessage(),
                    durationMs);
            });
    }

    @Override
    public int getOrder() {
        return FilterOrder.LOGGING;
    }
}