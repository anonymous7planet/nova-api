package com.nova.anonymousplanet.gateway.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.anonymousplanet.gateway.handler.GatewayExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration
 * fileName : GatewayConfiguration
 * author : Jinhong Min
 * date : 2025-11-15
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-15      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
public class GatewayConfiguration {
    // ObjectMapper는 WebFlux 환경에서 자동으로 Bean으로 등록되므로 주입받아 사용합니다.
    @Bean
    public ErrorWebExceptionHandler gatewayExceptionHandler(ObjectMapper objectMapper) {
        return new GatewayExceptionHandler(objectMapper);
    }

    // 필요하다면 ObjectMapper 설정을 여기서 커스터마이징 할 수 있습니다.
    /*
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Customizations...
        return mapper;
    }
    */
}
