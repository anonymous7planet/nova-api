package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.gateway.constant.LogContextCode;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : SlowLogFilter
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
@RequiredArgsConstructor
public class SlowLogFilter implements GlobalFilter, Ordered {
    private static final Logger slowLog = LoggerFactory.getLogger("slow.log");

    @Value("${nova.logging.slow.threshold-ms:1000}")
    private long slowThresholdMs;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Instant start = Instant.now();
        return chain.filter(exchange)
                .doOnEach(signal -> {
                    if(signal.isOnComplete()) {
                        long duration = Duration.between(start, Instant.now()).toMillis();
                        if (duration >= slowThresholdMs) {
                            logSlow(exchange, duration);
                        }
                    }
                });
    }

    private void logSlow(ServerWebExchange exchange, long duration) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpStatusCode statusCode = response.getStatusCode();
        int status = statusCode != null ? statusCode.value() : 0;

        Map<String, Object> logMap = new LinkedHashMap<>();
        logMap.put("traceId", MDC.get(LogContextCode.TRACE_ID.getMdcKey()));
        logMap.put("method", request.getMethod());
        logMap.put("path", request.getURI().getPath());
        logMap.put("status", status);
        logMap.put("duration", duration);

        String traceId = MDC.get(LogContextCode.TRACE_ID.getMdcKey());
        slowLog.warn("Slow Request Detected: [{}ms] {} {}",
                duration,
                request.getMethod(),
                request.getURI().getPath(),
                StructuredArguments.entries(Map.of(
                        "traceId", traceId != null ? traceId : "unknown",
                        "method", request.getMethod().name(),
                        "path", request.getURI().getPath(),
                        "duration", duration,
                        "logType", "slow"
                ))
        );
    }

    @Override
    public int getOrder() {
        return FilterOrder.SLOW_LOG;
    }

}
