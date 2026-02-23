package com.nova.anonymousplanet.security.configuration;

import com.nova.anonymousplanet.security.feign.NovaFeignInterceptor;
import com.nova.anonymousplanet.security.filter.NovaSecurityFilter;
import com.nova.anonymousplanet.security.handler.NovaAccessDeniedHandler;
import com.nova.anonymousplanet.security.handler.NovaAuthenticationEntryPoint;
import com.nova.anonymousplanet.security.provider.DiscoveryIpProvider;
import com.nova.anonymousplanet.security.provider.GatewayIpProvider;
import com.nova.anonymousplanet.security.resolver.NovaUserArgumentResolver;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.configuration
 * fileName : NovaSecurityAutoConfiguration
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
@EnableConfigurationProperties(NovaSecurityProperties.class) // 중요: 프로퍼티 활성화
@RequiredArgsConstructor
public class NovaSecurityAutoConfiguration implements WebMvcConfigurer {

    @Value("${nova.security.x-gateway-secret:}")
    private String gatewaySecret;

    @Value("${spring.application.name}")
    private String serviceName;
    /**
     * 1. 보안 설정 팩토리 빈 등록
     * 서비스의 SecurityFilterChain에서 공통 설정을 쉽게 불러올 수 있게 합니다.
     */
    // 1. 401 인증 예외 핸들러 빈 등록
    @Bean
    @ConditionalOnMissingBean
    public NovaAuthenticationEntryPoint novaAuthenticationEntryPoint() {
        return new NovaAuthenticationEntryPoint();
    }

    // 2. 403 인가 예외 핸들러 빈 등록
    @Bean
    @ConditionalOnMissingBean
    public NovaAccessDeniedHandler novaAccessDeniedHandler() {
        return new NovaAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public NovaSecurityConfigurer novaSecurityConfigurer(
            NovaSecurityFilter filter,
            GatewayIpProvider gatewayIpProvider,
            DiscoveryIpProvider discoveryIpProvider,
            NovaAuthenticationEntryPoint authenticationEntryPoint,
            NovaAccessDeniedHandler accessDeniedHandler,
            NovaSecurityProperties properties) { // 추가 주입
        return new NovaSecurityConfigurer(
                filter,
                gatewayIpProvider,
                discoveryIpProvider,
                authenticationEntryPoint,
                accessDeniedHandler,
                // List를 배열로 변환하여 전달
                properties.serviceWhiteList().toArray(String[]::new)
        );
    }

    /**
     * 2. 컨트롤러 파라미터 리졸버 등록
     * @CurrentUserId, @CurrentUserUserInfo 어노테이션을 처리합니다.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new NovaUserArgumentResolver());
    }

    /**
     * 3. 보안 필터 빈 등록
     * Gateway Secret 검증 및 UserContext(ThreadLocal) 적재를 담당합니다.
     */
    @Bean
    @ConditionalOnMissingBean
    public NovaSecurityFilter novaSecurityFilter(NovaAccessDeniedHandler novaAccessDeniedHandler) {
        return new NovaSecurityFilter(gatewaySecret, novaAccessDeniedHandler);
    }

    /**
     * 4. 서비스 간 통신(Feign) 인터셉터 등록
     * OpenFeign이 프로젝트에 포함되어 있을 때만 동작하며, 보안 헤더를 자동으로 전파합니다.
     */
    @Bean
    @ConditionalOnClass(name = "feign.RequestInterceptor")
    @ConditionalOnMissingBean
    public RequestInterceptor novaFeignInterceptor() {
        return new NovaFeignInterceptor(gatewaySecret, serviceName);
    }
}