package com.nova.anonymousplanet.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.anonymousplanet.gateway.dto.response.RestGatewayResponse;
import com.nova.anonymousplanet.gateway.exception.NovaGatewayAuthException;
import com.nova.anonymousplanet.gateway.filter.FilterOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.handler
 * fileName : GatewayExceptionHandler
 * author : Jinhong Min
 * date : 2025-11-15
 * description :

 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-15      Jinhong Min      최초 생성
 * ==============================================
 */
@Order(FilterOrder.EXCEPTION_HANDLER)
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GatewayExceptionHandler.class);
    private final ObjectMapper objectMapper;

    public GatewayExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        String requestId = exchange.getRequest().getHeaders().getFirst("X-Request-ID");
        String path = exchange.getRequest().getURI().getPath();

        // 이미 커밋된 응답이면 에러 전파
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 1. 상태 코드 및 에러 코드 매핑
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = "GATEWAY_ERROR";
        String detailMessage = "알 수 없는 게이트웨이 오류가 발생했습니다.";

        if (ex instanceof NovaGatewayAuthException) {
            status = HttpStatus.UNAUTHORIZED;
            code = "AUTH_UNAUTHORIZED";
            detailMessage = "인증에 실패했습니다: " + ex.getMessage();
        } else if (ex instanceof RuntimeException) {
//            status = HttpStatus.BAD_GATEWAY; // 내부 서비스 라우팅 오류 등
            code = "ROUTING_ERROR";
            detailMessage = ex.getMessage();
        }

        // 2. 응답 설정 및 DTO 구성
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        RestGatewayResponse.GatewayErrorSet errorSet =
            new RestGatewayResponse.GatewayErrorSet(path, code, detailMessage);

        RestGatewayResponse errorResponse = RestGatewayResponse.error(
            "요청 처리 중 오류 발생",
            requestId,
            errorSet
        );

        log.error("Gateway exception handled: id={} status={} code={} path={}",
            requestId, status.value(), code, path, ex);

        // 3. ObjectMapper를 사용하여 DTO를 JSON 바이트로 변환
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(errorResponse);
        } catch (JsonProcessingException e) {
            // JSON 변환 자체에 실패했을 경우를 대비한 대체 응답
            bytes = "{\"error\":\"Serialization Failed\",\"message\":\"Could not serialize error response.\"}".getBytes(
                StandardCharsets.UTF_8);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 응답 작성 및 반환
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}
