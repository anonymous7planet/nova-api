package com.nova.anonymousplanet.gateway.configuration;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration
 * fileName : SwaggerConfiguration
 * author : Jinhong Min
 * date : 2026-03-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-04      Jinhong Min      최초 생성
 * ==============================================
 */

@Configuration
@Profile({"local", "dev"}) // 운영(prod) 환경에서는 이 설정 클래스 자체가 로드되지 않음
@RequiredArgsConstructor
public class SwaggerConfiguration {

    @Bean
    @Primary
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        SwaggerUiConfigProperties properties = new SwaggerUiConfigProperties();
        // 핵심: 우리가 만든 동적 경로만 바라보게 강제 고정
        properties.setConfigUrl("/swagger-dynamic");
        properties.setDisableSwaggerDefaultUrl(true); // defaultUrl 수정 불가
        properties.setUrl(""); // Petstore URL 제거
        return properties;
    }

    @Bean
    @Primary
    public SpringDocConfigProperties springDocConfigProperties() {
        SpringDocConfigProperties properties = new SpringDocConfigProperties();

        // 1. Gateway 자체의 API 문서화 기능 완전 차단
        properties.getApiDocs().setEnabled(false);

        // 2. 컨트롤러가 있는 패키지를 스캔 대상에서 명시적으로 제외 (필요시)
        // properties.setPackagesToScan(List.of("none"));

        return properties;
    }

}
