package com.nova.anonymousplanet.logging.filter;

import com.nova.anonymousplanet.logging.util.LoggingConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * ReactiveMdcWebFilter (WebFlux)
 *
 * - Reactive 환경에서는 ThreadLocal(MDC) 사용이 바로 동작하지 않는다.
 * - 이 필터는 요청 헤더를 읽어 reactor context 에 MDC 맵을 넣고,
 *   로그에서 별도 구성으로 추적할 수 있도록 한다.
 *
 * NOTE: 로깅 프레임워크에서 ReactorContext -> MDC 연동 설정이 필요하거나,
 *       로그 출력 시 명시적으로 MDC 값을 읽어 출력하는 방식을 병행하세요.
 */
@Slf4j
public class ReactiveMdcWebFilter implements WebFilter, Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        Map<String, String> mdcMap = new HashMap<>();
        putIfPresent(mdcMap, LoggingConstants.MDC_TRACE, request.getHeaders().getFirst(LoggingConstants.HEADER_TRACE_ID));
        putIfPresent(mdcMap, LoggingConstants.MDC_REQUEST, request.getHeaders().getFirst(LoggingConstants.HEADER_REQUEST_ID));
        putIfPresent(mdcMap, LoggingConstants.MDC_USER_ID, request.getHeaders().getFirst(LoggingConstants.HEADER_USER_ID));
        putIfPresent(mdcMap, LoggingConstants.MDC_USER_UUID, request.getHeaders().getFirst(LoggingConstants.HEADER_USER_UUID));
        putIfPresent(mdcMap, LoggingConstants.MDC_USER_ROLE, request.getHeaders().getFirst(LoggingConstants.HEADER_USER_ROLE));
        putIfPresent(mdcMap, LoggingConstants.MDC_CLIENT_IP, request.getHeaders().getFirst(LoggingConstants.HEADER_CLIENT_IP));
        putIfPresent(mdcMap, LoggingConstants.MDC_DEVICE, request.getHeaders().getFirst(LoggingConstants.HEADER_DEVICE));

        // Reactor Context에 MDC 맵 주입
        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put("mdc", mdcMap))
                .doOnEach(signal -> {
                    // 간단 debug (주의: production에서는 log level 컨트롤)
                    if (log.isDebugEnabled() && !mdcMap.isEmpty()) {
                        log.debug("[ReactiveMdcWebFilter] injected mdc keys={}", mdcMap.keySet());
                    }
                });
    }

    private void putIfPresent(Map<String, String> map, String key, String value) {
        if (value != null && !value.isBlank()) map.put(key, value);
    }
}
