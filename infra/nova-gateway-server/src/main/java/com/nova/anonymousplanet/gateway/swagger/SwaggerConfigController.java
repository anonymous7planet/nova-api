package com.nova.anonymousplanet.gateway.swagger;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.swagger
 * fileName : SwaggerConfigController
 * author : Jinhong Min
 * date : 2026-03-06
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-06      Jinhong Min      최초 생성
 * ==============================================
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@Hidden
@Profile({"local", "dev"}) // local, dev 외의 환경(prod 등)에서는 빈으로 등록되지 않음
public class SwaggerConfigController {

    private final SwaggerServiceResolver swaggerServiceResolver;

    @GetMapping("/swagger-dynamic")
    public Map<String, Object> swaggerConfig() {
        log.info("Swagger UI 목록 실시간 갱신 요청 수신 - Eureka 조회 시작");
        return swaggerServiceResolver.getSwaggerUrls();
    }
}
