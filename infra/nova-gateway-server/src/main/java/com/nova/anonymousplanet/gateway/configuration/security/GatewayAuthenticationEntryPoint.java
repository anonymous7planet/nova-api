package com.nova.anonymousplanet.gateway.configuration.security;

import com.nova.anonymousplanet.gateway.constant.GatewayErrorCode;
import com.nova.anonymousplanet.gateway.exception.NovaGatewayAuthException;
import com.nova.anonymousplanet.gateway.util.NovaGatewayResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration.security
 * fileName : GatewayAuthenticationEntryPoint
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
@RequiredArgsConstructor
public class GatewayAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.error("인증 실패: {}", ex.getMessage());

        if(ex instanceof NovaGatewayAuthException novaAuth) {
            log.error(">>>> [Security EntryPoint] JWT 인증 실패: {} (Code: {})", novaAuth.getCustomMessage(), novaAuth.getErrorCode().getFullCode());
            return NovaGatewayResponseUtils.sendError(exchange, novaAuth.getErrorCode(), novaAuth.getCustomMessage());
        }

        // Project Nova의 공통 에러 코드 중 401에 해당하는 코드 사용 (예: INVALID_TOKEN)
        return NovaGatewayResponseUtils.sendError(
                exchange,
                GatewayErrorCode.UNAUTHORIZED,
                "인증 정보가 유효하지 않거나 만료되었습니다."
        );
    }
}
