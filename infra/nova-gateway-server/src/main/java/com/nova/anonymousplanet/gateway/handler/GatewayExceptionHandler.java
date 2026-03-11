package com.nova.anonymousplanet.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.core.constant.error.CommonErrorCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.model.response.NovaErrorResponse;
import com.nova.anonymousplanet.core.model.response.NovaResponse;
import com.nova.anonymousplanet.gateway.constant.GatewayErrorCode;
import com.nova.anonymousplanet.gateway.filter.order.FilterOrder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
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
@Order(FilterOrder.GLOBAL_ERROR_HANDLER)
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
        ErrorCode errorCode = resolveErrorCode(ex);
        response.setStatusCode(errorCode.getStatus());

        // 3. 표준 응답 객체 생성 (RestGatewayResponse)
        String traceId = getTraceId(exchange);
        String path = exchange.getRequest().getPath().value();

        NovaResponse<Void> errorBody = NovaResponse.fail("실패", traceId, traceId, path, NovaErrorResponse.of(errorCode));

        // [핵심] 로그 레벨 분기 실행
        logBySeverity(errorCode, ex, traceId, path);

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


    private ErrorCode resolveErrorCode(Throwable ex) {
        return switch (ex) {
            // 1. 특정 예외 타입 패턴 매칭
            case MethodNotAllowedException e -> GatewayErrorCode.METHOD_NOT_ALLOWED;
            case ConnectException e          -> GatewayErrorCode.SERVICE_UNAVAILABLE;
            case TimeoutException e          -> CommonErrorCode.GATEWAY_TIMEOUT;

            // 2. ResponseStatusException 처리
            case ResponseStatusException rse -> resolveResponseStatus(rse);

            // 3. Cause(원인)에 타임아웃이 포함된 경우 (Guarded Pattern 사용)
            case Throwable t when t.getCause() instanceof TimeoutException
                    -> CommonErrorCode.GATEWAY_TIMEOUT;

            // 4. 기본값
            default -> CommonErrorCode.INTERNAL_SERVER_ERROR;
        };
    }

    /**
     * HTTP 상태 코드에 따른 세부 매핑
     */
    private ErrorCode resolveResponseStatus(ResponseStatusException rse) {
        return switch (rse.getStatusCode().value()) {
            case 400 -> CommonErrorCode.BAD_REQUEST;
            case 401 -> GatewayErrorCode.UNAUTHORIZED;
            case 403 -> CommonErrorCode.FORBIDDEN;
            case 404 -> GatewayErrorCode.SERVICE_NOT_FOUND;
            case 405 -> GatewayErrorCode.METHOD_NOT_ALLOWED;
            case 429 -> CommonErrorCode.TOO_MANY_REQUESTS;
            case 503 -> GatewayErrorCode.SERVICE_UNAVAILABLE;
            case 504 -> CommonErrorCode.GATEWAY_TIMEOUT;
            default  -> CommonErrorCode.INTERNAL_SERVER_ERROR;
        };
    }

    /**
     * 에러 코드의 심각도에 따라 로그 레벨을 동적으로 결정
     */
    private void logBySeverity(ErrorCode errorCode, Throwable ex, String traceId, String path) {
        String logMessage = String.format(
                "[%s] TraceId: %s, Path: %s, Code: %s, Message: %s",
                errorCode.getName(), traceId, path, errorCode.getCode(), ex.getMessage()
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
