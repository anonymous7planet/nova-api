package com.nova.anonymousplanet.web.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
@Profile({"local", "dev"})
@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nova API")
                        .description("Nova MSA API Documentation")
                        .version("v1.0.0"));
    }
}
