package com.nova.anonymousplanet.core.filter;

import com.nova.anonymousplanet.core.constant.LogContextCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
        return chain.filter(exchange)
                .contextWrite(ctx -> {
                    // 1. 기존 Context에서 mdc 맵을 가져오거나 새로 생성
                    Map<String, String> mdcMap = ctx.getOrDefault("mdc", new HashMap<>());
                    ServerHttpRequest request = exchange.getRequest();

                    for (LogContextCode code : LogContextCode.values()) {
                        if (!code.isMdcSupport()) continue;

                        String mdcKey = code.getMdcKey();

                        // 2. Request ID는 추적의 핵심이므로 무조건 새로 생성되기 때문에 갱신)
                        if (code == LogContextCode.REQUEST_ID) {
                            String requestId = exchange.getAttribute(LogContextCode.REQUEST_ID.getMdcKey());
                            if (requestId != null) MDC.put(mdcKey, requestId);
                            continue;
                        }

                        // 3. 핵심 요구사항: MDC(Context Map)에 이미 값이 없을 때만 Put
                        if (!mdcMap.containsKey(mdcKey) || mdcMap.get(mdcKey).isBlank()) {
                            String value = resolveValue(exchange, code);
                            if (value != null && !value.isBlank()) {
                                mdcMap.put(mdcKey, value);
                            }
                        }
                    }

                    if (log.isDebugEnabled() && !mdcMap.isEmpty()) {
                        log.debug("[ReactiveMdcWebFilter] mdc populated: {}", mdcMap.keySet());
                    }

                    // 3. 갱신된 mdcMap을 다시 Context에 저장
                    return ctx.put("mdc", mdcMap);
                });
    }

    private String resolveValue(ServerWebExchange exchange, LogContextCode code) {
        // 1. TraceAndRequestIdFilter에서 저장한 Attribute 확인
        Object attr = exchange.getAttribute(code.getMdcKey());
        if (attr != null) return attr.toString();

        // 2. Client IP 특수 처리 (Reactive용 유틸리티가 있다면 활용)
        if (code == LogContextCode.CLIENT_IP) {
            String xff = exchange.getRequest().getHeaders().getFirst(code.getHeaderKey());
            return (xff != null) ? xff.split(",")[0].trim() : exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }

        // 3. 나머지는 100% 외부 헤더에서 가져옴
        return exchange.getRequest().getHeaders().getFirst(code.getHeaderKey());
    }

}
