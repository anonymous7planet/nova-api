package com.nova.anonymousplanet.logging.configuration;

import com.nova.anonymousplanet.logging.filter.MdcClearFilter;
import jakarta.servlet.Filter;
import com.nova.anonymousplanet.logging.filter.MdcFilter;
import com.nova.anonymousplanet.logging.filter.ReactiveMdcWebFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.logging.configuration
 * fileName : LoggingConfiguration
 * author : Jinhong Min
 * date : 2025-11-09
 * description :
 * - servlet(app) 환경에서는 MdcFilter 등록 (FilterRegistrationBean)
 * - reactive(app) 환경에서는 ReactiveMdcWebFilter를 빈으로 등록 (직접 WebFlux가 감지)
 *
 * 이 설정은 nova-logging 모듈을 dependency로 포함하면 자동으로 적용됩니다.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-09      Jinhong Min      최초 생성
 * ==============================================
 */

@Configuration(proxyBeanMethods = false)
public class LoggingConfiguration {
    // 추후 logback-spring.xml, MDC 필터, LogstashEncoder 설정 예정

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(name = "mdcFilterRegistration")
    public FilterRegistrationBean<Filter> mdcFilterRegistration() {
        FilterRegistrationBean<Filter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new MdcFilter());
        reg.setOrder(10); // 높은 우선순위로 적용 (필요시 조정)
        reg.addUrlPatterns("/*");
        return reg;
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnMissingBean
    public ReactiveMdcWebFilter reactiveMdcWebFilter() {
        return new ReactiveMdcWebFilter();
    }


    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(name = "mdcClearFilterRegistration")
    public FilterRegistrationBean<Filter> mdcClearFilterRegistration() {
        FilterRegistrationBean<Filter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new MdcClearFilter());
        reg.setOrder(9999); // 항상 가장 나중에 실행되도록
        reg.addUrlPatterns("/*");
        return reg;
    }
}
