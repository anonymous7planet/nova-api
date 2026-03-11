package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.gateway.configuration.security.model.UserPrincipal;
import com.nova.anonymousplanet.gateway.util.ClientUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : UserHeaderFilter
 * author : Jinhong Min
 * date : 2026-03-10
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-10      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Component
public class UserHeaderFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 공통 정보 추출 (인증 여부와 상관없음)
        String originalUrl = getOriginalPath(exchange);
        String clientIp = ClientUtils.getClientIp(exchange.getRequest());  // X-Forwarded-For 우선, 없으면 remote address 사용

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .cast(UsernamePasswordAuthenticationToken.class)
                .map(auth -> (UserPrincipal) auth.getPrincipal())
                // 1. 인증 정보가 있는 경우: 헤더를 주입한 요청으로 진행
                .flatMap(principal -> {
                    log.info("[UserHeaderFilter] Authenticated User: {}, IP: {}", principal.userId(), clientIp);
                    return chain.filter(injectHeaders(exchange, principal, originalUrl , clientIp));
                })
                // 2. 인증 정보가 없는 경우: 여기서 switchIfEmpty가 작동합니다!
                // ReactiveSecurityContextHolder가 비어있기 때문이죠.
                // 3. 인증 정보가 없는 경우: 공통 정보(Path, IP)만 주입하여 진행
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("[UserHeaderFilter] Anonymous User, IP: {}", clientIp);
                    return chain.filter(injectCommonHeaders(exchange, originalUrl, clientIp));
                }));
    }

    private String getOriginalPath(ServerWebExchange exchange) {
        Set<URI> originalUris = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        if (originalUris != null && !originalUris.isEmpty()) {
            // 첫 번째 값이 실제 클라이언트가 요청한 최초의 원본 URL입니다.
            return originalUris.iterator().next().getPath();
        }
        // 혹시 속성이 비어있다면 현재 요청의 경로를 사용
        return exchange.getRequest().getURI().getPath();
    }

    /**
     * 공통 헤더만 주입 (비로그인 사용자용)
     */
    private ServerWebExchange injectCommonHeaders(ServerWebExchange exchange, String requestPath, String clientIp) {
        return exchange.mutate()
                .request(r -> r.header(LogContextCode.REQUEST_PATH.getHeaderKey(), requestPath)
                        .header(LogContextCode.CLIENT_IP.getHeaderKey(), clientIp))
                .build();
    }

    private ServerWebExchange injectHeaders(ServerWebExchange exchange, UserPrincipal principal, String requestPath, String clientIp) {
        return exchange.mutate()
                .request(r -> r.header(LogContextCode.USER_ID.getHeaderKey(), String.valueOf(principal.userId()))
                        .header(LogContextCode.USER_UUID.getHeaderKey(), principal.uuid())
                        .header(LogContextCode.USER_ROLE.getHeaderKey(), principal.role())
                        .header(LogContextCode.USER_STATUS.getHeaderKey(), principal.userStatus())
                        .header(LogContextCode.REQUEST_PATH.getHeaderKey(), requestPath)
                        .header(LogContextCode.CLIENT_IP.getHeaderKey(), clientIp)
                ).build();
    }

    @Override
    public int getOrder() {
        return FilterOrder.USER_HEADER;
    }
}
