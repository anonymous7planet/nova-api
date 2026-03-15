package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import com.nova.anonymousplanet.gateway.util.NovaGatewayResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : CustomHeaderWebFilter
 * author : Jinhong Min
 * date : 2026-03-15
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-15      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
public class CustomHeaderFilter implements GlobalFilter, Ordered {

    @Value("${nova.security.client-header-value:aaa}")
    private String expectedHeaderValue;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientHeader = exchange.getRequest().getHeaders().getFirst(LogContextCode.NOVA_SECRET.getHeaderKey());

        // 1. 헤더 검증: 없거나 값이 틀리면 즉시 차단
        if (clientHeader == null || !clientHeader.equals(expectedHeaderValue)) {
            return NovaGatewayResponseUtils.sendError(exchange, CommonErrorCode.FORBIDDEN, "Header에 Nova-Secret정보를 확인해주세요.");
        }

        // 2. 검증 성공 시 다음 필터(JWT 필터 등)로 진행
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return FilterOrder.CUSTOM_HEADER;
    }
}
