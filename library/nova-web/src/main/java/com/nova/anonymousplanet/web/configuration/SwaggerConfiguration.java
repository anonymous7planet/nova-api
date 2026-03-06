package com.nova.anonymousplanet.web.configuration;

import com.nova.anonymousplanet.core.constant.SecurityConstants;
import com.nova.anonymousplanet.core.filter.FilterOrder;
import com.nova.anonymousplanet.web.filter.SwaggerKeyFilter;
import com.nova.anonymousplanet.web.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.List;

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
@Profile({"local", "dev"})
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "nova.swagger", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration {

    private final SwaggerProperties properties;
    private final SwaggerKeyFilter swaggerKeyFilter;

    private static final String BEARER_TOKEN_NAME = "Bearer_Auth";

    @Value("${spring.application.name}")
    private String serviceName;

    @Bean
    public FilterRegistrationBean<SwaggerKeyFilter> swaggerKeyFilterRegistration() {
        FilterRegistrationBean<SwaggerKeyFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(swaggerKeyFilter);
        // 필터가 적용될 URL 패턴 명시 (yml 설정과 동기화)
        registrationBean.setUrlPatterns(List.of(SecurityConstants.SERVLET_WHITE_LIST));
        // 가장 높은 우선순위로 설정하여 보안 강화
        registrationBean.setOrder(FilterOrder.SWAGGER_KEY_FILTER);

        return registrationBean;
    }

    @Bean
    @Primary
    public SpringDocConfigProperties springDocConfigProperties() {
        SpringDocConfigProperties props = new SpringDocConfigProperties();
        SpringDocConfigProperties.ApiDocs apiDocs = new SpringDocConfigProperties.ApiDocs();
        apiDocs.setEnabled(properties.enabled());
        props.setApiDocs(apiDocs);
        return props;
    }


    @Bean
    @Primary
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        SwaggerUiConfigProperties props = new SwaggerUiConfigProperties();
        props.setEnabled(properties.enabled());
        props.setPath("/swagger-ui.html");
        props.setDisableSwaggerDefaultUrl(true); // Default Swagger Petstore URL 노출 방지
        props.setDisplayRequestDuration(true);   // API 호출 소요 시간 표시
        return props;
    }


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(new Server().url("/api/"+serviceName.split("-")[1])))
                .addSecurityItem(new SecurityRequirement()
                        .addList(properties.headerName())
                        .addList(properties.gatewayHeader())
                        .addList(BEARER_TOKEN_NAME))
                .components(new Components()
                        // 1. 특정 키 인증 (application.yml 기반)
                        .addSecuritySchemes(properties.headerName(), new SecurityScheme()
                                .name(properties.headerName())
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("Swagger 사용을 위한 인증 키를 입력하세요."))

                        // 2. 게이트웨이 보안 헤더 (Nova 보안 전략)
                        .addSecuritySchemes(properties.gatewayHeader(), new SecurityScheme()
                                .name(properties.gatewayHeader())
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("Gateway 통신 보안 키"))

                        // 3. JWT 인증 (Bearer)
                        .addSecuritySchemes(BEARER_TOKEN_NAME, new SecurityScheme()
                                .name(BEARER_TOKEN_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    private Info apiInfo() {
        return new Info()
                .title("Project Nova - Danby API Document")
                .description("Nova 프로젝트의 통합 API 문서 (Java 21 / Spring Boot 3.4.4)")
                .version("1.0.0")
                .contact(new Contact().name("Nova Development Team"));
    }
}
