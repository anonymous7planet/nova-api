package com.nova.anonymousplanet.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Global logging filter for gateway.
 * - sets traceId in MDC
 * - logs request path, method, remote address and response status
 */
@Slf4j
@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID = "traceId";
    private static final String USER_ID = "userId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);
        try {
            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().toString();
            String clientIp = exchange.getRequest().getRemoteAddress() != null ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() : "unknown";

            log.info("[REQ] {} {} from {}", method, path, clientIp);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Integer status = exchange.getResponse().getStatusCode() != null ? exchange.getResponse().getStatusCode().value() : null;
                String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
                if (userId != null) {
                    MDC.put(USER_ID, userId);
                }
                log.info("[RES] {} {} -> status={}", method, path, status);
            }));
        } finally {
            MDC.remove(TRACE_ID);
            MDC.remove(USER_ID);
        }
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
