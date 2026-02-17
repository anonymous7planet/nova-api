package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : LocalMasterTokenFilter
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */
// FIXME : 작업중
@Component
@Profile("local") // 핵심: application.yml의 active profile이 local일 때만 동작
public class LocalMasterTokenFilter implements GlobalFilter, Ordered {

    private static final String MASTER_TOKEN = "nova-static-debug-token";
    private static final String GATEWAY_SECRET = "nova-secret-key"; // 내부 보안 헤더용

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        // 마스터 토큰 검증 (로컬 환경 전용)
        if (authHeader != null && authHeader.equals("Bearer " + MASTER_TOKEN)) {
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-User-Id", "1") // 테스트용 Long ID
                    .header("X-User-UUID", "00000000-0000-0000-0000-000000000001")
                    .header("X-Gateway-Secret", GATEWAY_SECRET) // 보안 전략: Gateway 검증 증표
                    .build();

            return chain.filter(exchange.mutate().request(request).build());
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 실제 JWT 검증 필터보다 우선순위를 높게 설정 (낮은 숫자)
        return FilterOrder.LOCAL_MASTER;
    }
}
