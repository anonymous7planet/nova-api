package com.nova.anonymousplanet.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.anonymousplanet.gateway.constant.GatewayErrorCode;
import com.nova.anonymousplanet.gateway.constant.LogContextCode;
import com.nova.anonymousplanet.gateway.dto.response.RestGatewayResponse;
import com.nova.anonymousplanet.gateway.exception.NovaGatewayAuthException;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.utils.LogContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

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
@RequiredArgsConstructor
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    // Logger 이름을 "error.log"로 명시해야 XML의 <logger name="error.log">와 매칭됩니다.
    private static final Logger log = LoggerFactory.getLogger("error.log");

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 1. 공통 응답 헤더 설정
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 2. 예외 분석 및 에러 코드 매핑
        GatewayErrorCode errorCode = resolveErrorCode(ex);
        HttpStatus status = resolveHttpStatus(errorCode);
        response.setStatusCode(status);

        // 3. 표준 응답 객체 생성 (RestGatewayResponse)
        String traceId = getTraceId(exchange);
        String path = exchange.getRequest().getPath().value();

        RestGatewayResponse errorBody = RestGatewayResponse.error(
                path,
                traceId,
                new RestGatewayResponse.GatewayErrorSet(
                        errorCode.getCode(),
                        errorCode.getTitleMessage(),
                        ex.getMessage() != null ? ex.getMessage() : errorCode.getDetailMessage()
                )
        );

        // [핵심] 로그 레벨 분기 실행
        logBySeverity(errorCode, ex, traceId, exchange.getRequest().getPath().value());

        // 5. 응답 본문 작성
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(errorBody));
            } catch (JsonProcessingException e) {
                log.error("JSON Serialization Error: {}", e.getMessage());
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }

    private String getTraceId(ServerWebExchange exchange) {
        // 1. TraceIdFilter에서 넣은 속성 확인
        String traceId = exchange.getAttribute(LogContextCode.TRACE_ID.getMdcKey());

        // 2. 만약 필터에서 에러가 발생하여 속성이 없다면, 헤더에서 직접 추출
        if (traceId == null) {
            traceId = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(LogContextCode.TRACE_ID.getHeaderKey()))
                    .orElse("UNKNOWN");
        }

        return traceId;
    }


    private GatewayErrorCode resolveErrorCode(Throwable ex) {
        if (ex instanceof ResponseStatusException rse) {
            return switch (rse.getStatusCode().value()) {
                case 404 -> GatewayErrorCode.SERVICE_NOT_FOUND;
                case 401 -> GatewayErrorCode.INVALID_TOKEN;
                case 403 -> GatewayErrorCode.FORBIDDEN;
                default -> GatewayErrorCode.INTERNAL_SERVER_ERROR;
            };
        }

        if (ex instanceof MethodNotAllowedException) return GatewayErrorCode.METHOD_NOT_ALLOWED;
        if (ex instanceof ConnectException) return GatewayErrorCode.SERVICE_UNAVAILABLE;
        if (ex instanceof TimeoutException) return GatewayErrorCode.GATEWAY_TIMEOUT;

        return GatewayErrorCode.INTERNAL_SERVER_ERROR;
    }

    private HttpStatus resolveHttpStatus(GatewayErrorCode errorCode) {
        return switch (errorCode.getCode().substring(1, 4)) {
            case "400" -> HttpStatus.BAD_REQUEST;
            case "401" -> HttpStatus.UNAUTHORIZED;
            case "403" -> HttpStatus.FORBIDDEN;
            case "404" -> HttpStatus.NOT_FOUND;
            case "429" -> HttpStatus.TOO_MANY_REQUESTS;
            case "503" -> HttpStatus.SERVICE_UNAVAILABLE;
            case "504" -> HttpStatus.GATEWAY_TIMEOUT;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }


    /**
     * 에러 코드의 심각도에 따라 로그 레벨을 동적으로 결정
     */
    private void logBySeverity(GatewayErrorCode errorCode, Throwable ex, String traceId, String path) {
        String logMessage = String.format(
                "[%s] TraceId: %s, Path: %s, Code: %s, Message: %s",
                errorCode.name(), traceId, path, errorCode.getCode(), ex.getMessage()
        );

        // G5로 시작하는 코드는 시스템 장애(ERROR), 그 외는 단순 경고(WARN) 또는 정보(INFO)
        if (errorCode.getCode().startsWith("G5")) {
            // Critical Error: 스택 트레이스를 포함하여 상세히 기록 (Sentry/ELK 알람 대상)
            log.error(logMessage, ex);
        } else if (errorCode.getCode().startsWith("G401") || errorCode.getCode().startsWith("G403")) {
            // Security Warning: 보안 관련 이슈는 경고 수준으로 기록
            log.warn(logMessage);
        } else {
            // 일반적인 클라이언트 요청 오류
            log.info(logMessage);
        }
    }
}
