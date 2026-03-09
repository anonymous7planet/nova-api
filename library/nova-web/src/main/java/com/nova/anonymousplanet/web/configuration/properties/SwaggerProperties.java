package com.nova.anonymousplanet.web.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.web.properties
 * fileName : SwaggerProperties
 * author : Jinhong Min
 * date : 2026-03-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-04      Jinhong Min      최초 생성
 * ==============================================
 */

@ConfigurationProperties(prefix = "nova.swagger")
public record SwaggerProperties(
        @DefaultValue("false") boolean enabled,
        @DefaultValue("X-Nova-Swagger-Key") String headerName,
        @DefaultValue("nova-default-secret") String headerValue,
        @DefaultValue("X-Gateway-Secret") String gatewayHeader,
        @DefaultValue("nova-gateway-default-key") String gatewaySecret
) {}