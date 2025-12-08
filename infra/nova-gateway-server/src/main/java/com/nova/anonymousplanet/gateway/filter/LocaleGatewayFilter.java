package com.nova.anonymousplanet.gateway.filter;

import com.nova.anonymousplanet.gateway.constant.LogHeaderCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.filter
 * fileName : LocaleGatewayFilter
 * author : Jinhong Min
 * date : 2025-12-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-04      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
@Component
public class LocaleGatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String localeHeader = request.getHeaders().getFirst(LogHeaderCode.LOCALE.getKey());

        if (localeHeader == null || localeHeader.isBlank()) {
            String lang = request.getHeaders().getFirst(LogHeaderCode.LANG.getKey());
            if (lang != null && !lang.isBlank()) {
                localeHeader = Locale.forLanguageTag(lang).toLanguageTag();
                log.debug("[LocaleGatewayFilter] X-Lang -> normalized localeHeader={}", localeHeader);
            }
        } else {
            localeHeader = Locale.forLanguageTag(localeHeader).toLanguageTag();
            log.debug("[LocaleGatewayFilter] X-Locale provided normalized={}", localeHeader);
        }

        if (localeHeader == null || localeHeader.isBlank()) {
            String acceptLang = request.getHeaders().getFirst(LogHeaderCode.ACCEPT_LANGUAGE.getKey());
            if (acceptLang != null && !acceptLang.isBlank()) {
                String first = acceptLang.split(",")[0].trim();
                localeHeader = Locale.forLanguageTag(first).toLanguageTag();
                log.debug("[LocaleGatewayFilter] Accept-Language -> resolved localeHeader={}", localeHeader);
            }
        }

        if (localeHeader == null || localeHeader.isBlank()) {
            localeHeader = Locale.getDefault().toLanguageTag();
            log.debug("[LocaleGatewayFilter] No lang found - default locale applied: {}", localeHeader);
        }

        ServerHttpRequest mutated = request.mutate()
                .header(LogHeaderCode.LOCALE.getKey(), localeHeader)
                .header(LogHeaderCode.ACCEPT_LANGUAGE.getKey(), localeHeader)
                .build();

        log.info("[LocaleGatewayFilter] Locale resolved and injected: {}", localeHeader);

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    @Override
    public int getOrder() {
        return FilterOrder.LOCALE;
    }
}
