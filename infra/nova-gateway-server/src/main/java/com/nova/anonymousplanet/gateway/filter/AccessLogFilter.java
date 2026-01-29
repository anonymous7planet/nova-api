package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.gateway.constant.LogContextCode;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : AccessLogFilter
 * author : Jinhong Min
 * date : 2026-01-26
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-26      Jinhong Min      최초 생성
 * ==============================================
 */
@Component
public class AccessLogFilter implements GlobalFilter, Ordered {
    private static final Logger accessLog = LoggerFactory.getLogger("access.log");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Instant start = Instant.now();

        return chain.filter(exchange)
                .doOnEach(signal -> {
                    if (signal.isOnComplete()) {
                        logAccess(exchange, start);
                    }
                });
    }

    private void logAccess(ServerWebExchange exchange, Instant start) {
        long duration = Duration.between(start, Instant.now()).toMillis();

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpStatusCode statusCode = response.getStatusCode();
        int status = statusCode != null ? statusCode.value() : 0;

        Map<String, Object> logMap = new LinkedHashMap<>();
        logMap.put("traceId", MDC.get(LogContextCode.TRACE_ID.getMdcKey()));
        logMap.put("method", request.getMethod());
        logMap.put("path", request.getURI().getPath());
        logMap.put("query", request.getURI().getQuery());
        logMap.put("status", status);
        logMap.put("duration", duration);
        logMap.put("clientIp", MDC.get(LogContextCode.CLIENT_IP.getMdcKey()));
        logMap.put("userAgent", MDC.get(LogContextCode.USER_AGENT.getMdcKey()));

        accessLog.info("access.log");

    }

    private void logAccess(ServerWebExchange exchange, Instant start, SignalType signalType) {

        long duration = Duration.between(start, Instant.now()).toMillis();

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpStatusCode statusCode = response.getStatusCode();
        int status = statusCode != null ? statusCode.value() : 0;

        Map<String, Object> logMap = new LinkedHashMap<>();
        logMap.put("traceId", MDC.get(LogContextCode.TRACE_ID.getMdcKey()));
        logMap.put("method", request.getMethod());
        logMap.put("path", request.getURI().getPath());
        logMap.put("query", request.getURI().getQuery());
        logMap.put("status", status);
        logMap.put("duration", duration);
        logMap.put("clientIp", MDC.get(LogContextCode.CLIENT_IP.getMdcKey()));
        logMap.put("userAgent", MDC.get(LogContextCode.USER_AGENT.getMdcKey()));
        logMap.put("signal", signalType.name());

    }


    @Override
    public int getOrder() {
        return FilterOrder.ACCESS_LOG;
    }
}
