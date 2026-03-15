package com.nova.anonymousplanet.core.configuration.feign;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.anonymousplanet.core.util.JsonUtils;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.configuration.feign
 * fileName : NovaErrorDecoder
 * author : Jinhong Min
 * date : 2026-03-15
 * description :
 * FIXME: NovaApplicationException 수정필요
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-15      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
public class NovaErrorDecoder implements ErrorDecoder {

    public ObjectMapper getMapper() {
        return JsonUtils.getMapper();
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = "empty";
        try (InputStream inputStream = response.body().asInputStream()) {
            byte[] rawBytes = inputStream.readAllBytes();
            body = new String(rawBytes, StandardCharsets.UTF_8);

            // NovaResponse 구조에서 error 필드만 추출
            JsonNode rootNode = getMapper().readTree(body);
            JsonNode errorNode = rootNode.get("error");

            if (errorNode != null) {
                String code = errorNode.get("code").asText();
                String message = errorNode.get("message").asText();
                int status = errorNode.get("status").asInt();

                log.error("[Feign API Error] {} -> Code: {}, Message: {}", methodKey, code, message);

                // NovaApplicationException으로 복원 (ErrorCode 매핑 로직은 프로젝트에 맞게 보완 필요)
//                return new NovaApplicationException(code, message, status);
            }
        } catch (Exception e) {
            log.error("[Feign Parse Error] {} | Body: {} | Reason: {}", methodKey, body, e.getMessage());
        }

        return new RuntimeException("서비스 간 통신 중 알 수 없는 에러가 발생했습니다.");
    }
}
