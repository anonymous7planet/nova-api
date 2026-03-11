package com.nova.anonymousplanet.gateway.configuration;

import com.nova.anonymousplanet.core.constant.LogContextCode;
import com.nova.anonymousplanet.gateway.configuration.properties.NovaGatewaySecurityProperties;
import com.nova.anonymousplanet.gateway.filter.JwtAuthenticationGatewayFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GatewayRouteConfig
 * - Java-based routing configuration
 * - Each route applies logging and authentication filters
 */
@Configuration
@EnableConfigurationProperties(NovaGatewaySecurityProperties.class)
@RequiredArgsConstructor
public class GatewayRouteConfiguration {

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${nova.security.x-gateway-secret:}")
    private String gatewaySecret;

    @Value("${nova.swagger.header-name:X-Nova-Swagger-Key}")
    private String headerName;

    @Value("${nova.swagger.header-value:}")
    private String headerValue;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder,
//                                     JwtAuthenticationGatewayFilter jwtAuthenticationGatewayFilter,
                                     NovaGatewaySecurityProperties novaGatewaySecurityProperties // 👈 위에서 만든 클래스 주입
    ) {
        // 필터에 적용할 설정 객체 생성
//        JwtAuthenticationGatewayFilter.Config jwtAuthConfig = new JwtAuthenticationGatewayFilter.Config();
        // YML에서 읽어온 제외 경로 리스트를 주입
//        jwtAuthConfig.setNovaGatewaySecurityProperties(novaGatewaySecurityProperties);
//        GatewayFilter jwtFilter = jwtAuthenticationGatewayFilter.apply(jwtAuthConfig);

        return builder.routes()
//                .route("system-service", r -> createSystemServiceRoute(r, jwtFilter))
//                .route("notification-service", r -> createNotificationServiceRoute(r, jwtFilter))
//                .route("auth-service", r -> createAuthServiceRoute(r, jwtFilter))
                .route("system-service", this::createSystemServiceRoute)
                .route("notification-service", this::createNotificationServiceRoute)
                .route("auth-service", this::createAuthServiceRoute)
                .route("system-api-docs", this::createSystemServiceSwaggerRoute)
                .route("notification-api-docs", this::createNotificationServiceSwaggerRoute)
                .route("auth-api-docs", this::createAuthServiceSwaggerRoute)
                .build();
    }


    /**
     * System Service 라우팅 정의
     */
    private Buildable<Route> createSystemServiceRoute(PredicateSpec r) {
        return r.path("/api/system/**")
                .filters(
                        f -> applyCommonFilters(f)
                        // /api/system/ 부분을 뒤에 오는 모든 것($1)으로 교체
//                          .rewritePath("/api/system/(?<segment>.*)", "/${segment}")

                )
                .uri("lb://NOVA-SYSTEM-SERVICE");
    }

    /**
     * Notification Service 라우팅 정의
     */
    private Buildable<Route> createNotificationServiceRoute(PredicateSpec r) {
        return r.path("/api/notification/**")
                .filters(
                        f -> applyCommonFilters(f)
                )
                .uri("lb://NOVA-NOTIFICATION-SERVICE");
    }

    /**
     * Auth Service 라우팅 정의
     */
    private Buildable<Route> createAuthServiceRoute(PredicateSpec r) {
        return r
                .path("/api/auth/**")
                .filters(
                        f -> applyCommonFilters(f)
                )
                .uri("lb://NOVA-AUTH-SERVICE");
    }


    /**
     * 전사 공통 필터 적용 (Project Nova 보안 전략)
     * 1. Prefix 제거 (/api/service-name/** -> /**)
     * 2. JWT 인증 및 UUID -> Long ID 변환 필터 적용
     * 3. Gateway Secret 및 호출 서비스명 헤더 주입
     */
    private GatewayFilterSpec applyCommonFilters(GatewayFilterSpec f) {
        return f.stripPrefix(2)
//                .filter(jwtFilter)
                .addRequestHeader(LogContextCode.GATEWAY_SECRET.getHeaderKey(), gatewaySecret)
                .addRequestHeader(LogContextCode.SERVICE_NAME.getHeaderKey(), serviceName);
    }

    private Buildable<Route> createSystemServiceSwaggerRoute(PredicateSpec r) {
        return r
                .path("/nova-system-service/**")
                .filters(f -> f
                        .rewritePath("/nova-system-service/(?<segment>.*)", "/${segment}")
                        .addRequestHeader(LogContextCode.GATEWAY_SECRET.getHeaderKey(), gatewaySecret)
                        .addRequestHeader(headerName, headerValue)
                )
                .uri("lb://NOVA-SYSTEM-SERVICE");
    }

    private Buildable<Route> createNotificationServiceSwaggerRoute(PredicateSpec r) {
        return r
                .path("/nova-notification-service/**")
                .filters(f -> f
                        .rewritePath("/nova-notification-service/(?<segment>.*)", "/${segment}")
                        .addRequestHeader(LogContextCode.GATEWAY_SECRET.getHeaderKey(), gatewaySecret)
                        .addRequestHeader(headerName, headerValue)
                )
                .uri("lb://NOVA-NOTIFICATION-SERVICE");
    }

    private Buildable<Route> createAuthServiceSwaggerRoute(PredicateSpec r) {
        return r
                .path("/nova-auth-service/**")
                .filters(f -> f
                        .rewritePath("/nova-auth-service/(?<segment>.*)", "/${segment}")
                        .addRequestHeader(LogContextCode.GATEWAY_SECRET.getHeaderKey(), gatewaySecret)
                        .addRequestHeader(headerName, headerValue)
                )
                .uri("lb://NOVA-AUTH-SERVICE");
    }

    /**
     * @param builder
     * @return
     */
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//
//            // ------------------------------------------------------------------
//            // nova-auth-service
//            // ------------------------------------------------------------------
//            .route("auth-service", r -> r
//                .path("/api/auth/**")                    // /api/auth/** 경로를 nova-auth-service로 전달
//                .filters(f -> f
//                        .stripPrefix(1)                      // /api 제거 후 /v1/auth/** 로 전달
//                    // ======= 아래는 참고 가능한 필터 옵션들 =======
//                    // .addRequestHeader("X-Gateway", "nova-gateway")  // 요청 헤더 추가
//                    // .addResponseHeader("X-Response-Time", "#{T(System).currentTimeMillis()}")  // 응답 헤더 추가
//                    // .removeRequestHeader("Cookie")       // 특정 요청 헤더 제거
//                    // .rewritePath("/api/auth/(?<segment>.*)", "/${segment}") // 정규식 기반 경로 재작성
//                    // .prefixPath("/internal")             // 요청 경로 앞에 prefix 추가
//                    // .setPath("/newpath")                 // 요청 URI 자체 변경
//                    // .retry(config -> config.setRetries(3).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)) // 재시도 설정
//                    // .requestRateLimiter(config -> {      // 요청 제한 (Redis 필요)
//                    //     config.setRateLimiter(redis -> redis.setReplenishRate(5).setBurstCapacity(10));
//                    //     config.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
//                    // })
//                    // .circuitBreaker(config -> config     // 장애 격리용 회로차단기
//                    //     .setName("authCircuitBreaker")
//                    //     .setFallbackUri("forward:/fallback/auth"))
//                    // .preserveHostHeader()                // 원본 Host 헤더 유지
//                    // .rewriteResponseHeader("Location", "http://localhost", "https://nova.app") // 응답 헤더 재작성
//                )
//                .uri("lb://nova-auth-service"))          // 로드밸런서 기반 서비스 URI
//
//            // ------------------------------------------------------------------
//            // nova-user-service
//            // ------------------------------------------------------------------
//            .route("user-service", r -> r
//                .path("/api/users/**")
//                .filters(f -> f
//                        .stripPrefix(2)
//                    // .addRequestParameter("source", "gateway")   // 요청 파라미터 추가
//                    // .removeResponseHeader("Server")             // 응답 헤더 제거
//                    // .retry(retryConfig -> retryConfig
//                    //     .setRetries(2)
//                    //     .setStatuses(HttpStatus.BAD_GATEWAY, HttpStatus.INTERNAL_SERVER_ERROR))
//                    // .requestSize(10 * 1024 * 1024)              // 요청 크기 제한 (10MB)
//                )
//                .uri("lb://nova-user-service"))
//
//            // ------------------------------------------------------------------
//            // nova-matching-service
//            // ------------------------------------------------------------------
//            .route("matching-service", r -> r
//                .path("/api/matching/**")
//                .filters(f -> f
//                        .stripPrefix(2)
//                    // .hystrix(config -> config
//                    //     .setName("matchingFallback")
//                    //     .setFallbackUri("forward:/fallback/matching"))
//                    // .retry(retryConfig -> retryConfig.setRetries(3))
//                    // .requestRateLimiter(config -> config
//                    //     .setRateLimiter(redis -> redis.setReplenishRate(10).setBurstCapacity(20))
//                    //     .setStatusCode(HttpStatus.TOO_MANY_REQUESTS))
//                )
//                .uri("lb://nova-matching-service"))
//
//            // ------------------------------------------------------------------
//            // nova-admin-service (예: 관리자 페이지 API)
//            // ------------------------------------------------------------------
//            .route("admin-service", r -> r
//                .path("/api/admin/**")
//                .filters(f -> f
//                        .stripPrefix(2)
//                    // .addRequestHeader("X-Admin-Token", "true")
//                    // .circuitBreaker(config -> config
//                    //     .setName("adminCircuitBreaker")
//                    //     .setFallbackUri("forward:/fallback/admin"))
//                )
//                .uri("lb://nova-admin-service"))
//
//            .build();
//    }
}