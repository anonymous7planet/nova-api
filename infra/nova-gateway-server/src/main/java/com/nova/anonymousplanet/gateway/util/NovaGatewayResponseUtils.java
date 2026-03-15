package com.nova.anonymousplanet.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.model.response.NovaErrorResponse;
import com.nova.anonymousplanet.core.model.response.NovaResponse;
import com.nova.anonymousplanet.core.util.JsonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.util
 * fileName : NovaGatewayResponseUtils
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NovaGatewayResponseUtils {

    /**
     * core에서 관리하는 표준 ObjectMapper를 참조합니다.
     */
    private static ObjectMapper getObjectMapper() {
        return JsonUtils.getMapper();
    }

    /**
     * 실패 응답을 JSON 형태로 전송합니다.
     * * @param exchange  현재 서버 교환 객체
     * @param errorCode 전사 공통 에러 코드
     * @param message   사용자 정의 메시지 (null일 경우 에러 코드의 기본 메시지 사용)
     * @return Mono<Void>
     */
    public static Mono<Void> sendError(ServerWebExchange exchange, ErrorCode errorCode, String message) {
        return Mono.defer(() -> {
            // 1. HTTP 상태 코드 및 헤더 설정
            exchange.getResponse().setStatusCode(errorCode.getStatus());
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);


            // 2. NovaResponse.fail() 호출 시 내부적으로 TraceId 등이 주입되는 구조 유지
            // 2-1. 메시지
            String finalMessage = (message != null) ? message : errorCode.getMessage();
            // 2-2. TraceIdFilter에서 넣은 속성 확인
            String traceId = exchange.getAttribute(LogContextCode.TRACE_ID.getMdcKey());
            // 만약 필터에서 에러가 발생하여 속성이 없다면, 헤더에서 직접 추출
            if (traceId == null) {
                traceId = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(LogContextCode.TRACE_ID.getHeaderKey()))
                        .orElse("UNKNOWN");
            }
            // 2-3. 요청URL
            String path = exchange.getRequest().getPath().value();

            // 2-4. Project Nova 표준 에러 응답 객체 생성
            NovaErrorResponse errorDetail = NovaErrorResponse.of(errorCode);

            NovaResponse<Void> novaResponse = NovaResponse.fail(finalMessage, traceId, traceId, path, errorDetail);

            try {
                // 3. JSON 직렬화 및 버퍼 생성
                byte[] data = getObjectMapper().writeValueAsString(novaResponse).getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(data);

                log.error(">>>> [Gateway Error] Path: {}, Error: {}", exchange.getRequest().getPath(), finalMessage);

                return exchange.getResponse().writeWith(Mono.just(buffer));
            } catch (JsonProcessingException e) {
                log.error("JSON Serialization Error: {}", e.getMessage());
                return Mono.error(e);
            }
        });
    }

}
