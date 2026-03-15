package com.nova.anonymousplanet.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.model.response.NovaErrorResponse;
import com.nova.anonymousplanet.core.model.response.NovaResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.util
 * fileName : NovaFilterResponseUtils
 * author : Jinhong Min
 * date : 2026-03-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-04      Jinhong Min      최초 생성
 * ==============================================
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NovaResponseUtils {

    /**
     * core에서 관리하는 표준 ObjectMapper를 참조합니다.
     */
    private static ObjectMapper getObjectMapper() {
        return JsonUtils.getMapper();
    }

    /**
     * 에러 코드를 바탕으로 Nova 표준 실패 응답을 전송합니다.
     * @param response
     * @param errorCode
     * @param message: NovaResponse의 message
     * @throws IOException
     */
    public static void sendError(HttpServletResponse response, ErrorCode errorCode, String message) throws IOException {

        // 1. 에러 상세 정보 생성 (NovaErrorResponse)
        NovaErrorResponse error = NovaErrorResponse.of(errorCode);

        // 2. 표준 응답 객체 생성 (NovaResponse)
        // Compact Constructor에 의해 MDC 정보(traceId 등)가 자동 주입됨
        NovaResponse<Void> novaResponse = NovaResponse.fail(message != null ? message: "실패", error);

        // 3. HTTP 응답 헤더 및 상태 코드 설정
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 4. JSON 직렬화 및 본문 작성
        String json = getObjectMapper().writeValueAsString(novaResponse);
        response.getWriter().write(json);
    }

}
