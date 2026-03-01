package com.nova.anonymousplanet.web.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.configuration
 * fileName : SwaggerConfiguration
 * author : Jinhong Min
 * date : 2026-02-25
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-25      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
public class SwaggerConfiguration {

    @Value("${spring.application.name:nova-service}")
    private String applicationName;

    private static final String JWT_SCHEME = "JwtAuth";
    private static final String GATEWAY_HEADER = "X-Gateway-Secret";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Project Nova [" + applicationName.toUpperCase() + "] API")
                        .description("Danby Nova MSA - " + applicationName + " 명세서")
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement()
                        .addList(JWT_SCHEME)
                        .addList(GATEWAY_HEADER))
                .components(new Components()
                        .addSecuritySchemes(JWT_SCHEME, new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT"))
                        .addSecuritySchemes(GATEWAY_HEADER, new SecurityScheme()
                                .name(GATEWAY_HEADER)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("Gateway 전용 보안 헤더")));
    }

    /**
     * yml의 springdoc.api-docs.path 등을 대체하는 설정
     * 서비스별로 기본 그룹을 자동 생성합니다.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group(applicationName)
                .pathsToMatch("/**")
                .build();
    }

    /**
     * Swagger UI 화면 설정 (yml 설정을 자바로 대체)
     * @Primary를 통해 라이브러리 기본 설정을 덮어씌움
     */
    @Bean
    @Primary
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        SwaggerUiConfigProperties props = new SwaggerUiConfigProperties();
        props.setPath("/swagger-ui.html");       // 접속 경로
        props.setOperationsSorter("alpha");     // 알파벳순 정렬
        props.setDisplayRequestDuration(true);   // 실행 시간 표시
        return props;
    }

    /**
     * OpenAPI 문서 자체 설정 (yml 설정을 자바로 대체)
     */
    @Bean
    @Primary
    public SpringDocConfigProperties springDocConfigProperties() {
        SpringDocConfigProperties props = new SpringDocConfigProperties();

        // 1. ApiDocs 객체를 먼저 생성하고 설정을 세팅합니다.
        SpringDocConfigProperties.ApiDocs apiDocs = new SpringDocConfigProperties.ApiDocs();
        apiDocs.setPath("/v3/api-docs");

        // 2. 생성된 apiDocs 객체를 props에 주입합니다. (void 반환이므로 분리 실행)
        props.setApiDocs(apiDocs);

        // 3. 기본 미디어 타입 설정
        props.setDefaultProducesMediaType("application/json");

        return props;
    }
}
