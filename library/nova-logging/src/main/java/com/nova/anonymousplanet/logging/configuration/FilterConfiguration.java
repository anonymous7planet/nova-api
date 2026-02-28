package com.nova.anonymousplanet.logging.configuration;

import com.nova.anonymousplanet.logging.filter.MdcClearFilter;
import com.nova.anonymousplanet.logging.filter.MdcFilter;
import com.nova.anonymousplanet.logging.filter.ReactiveMdcWebFilter;
import com.nova.anonymousplanet.logging.filter.TraceAndRequestIdFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.configuration
 * fileName : FilterConfiguration
 * author : Jinhong Min
 * date : 2025-12-04
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-12-04      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration(proxyBeanMethods = false)
public class FilterConfiguration {


    @Value("${spring.application.name}")
    private String serviceName;

    // 필터 순서 상수 관리
    public static class FilterOrder {
        public static final int TRACE_FILTER = Ordered.HIGHEST_PRECEDENCE; // 최우선
        public static final int MDC_SETUP_FILTER = TRACE_FILTER + 1;                    // 로그 설정
        public static final int MDC_CLEAR_FILTER = Ordered.LOWEST_PRECEDENCE;                  // 최하단 (정리)
    }

    @Bean
    public FilterRegistrationBean<TraceAndRequestIdFilter> traceFilterRegistration() {
        FilterRegistrationBean<TraceAndRequestIdFilter> filter = new FilterRegistrationBean<>(new TraceAndRequestIdFilter(serviceName));
        filter.setOrder(FilterOrder.TRACE_FILTER); // 서비스 내에서 최우선으로 적용
        return filter;
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(name = "mdcFilterRegistration")
    public FilterRegistrationBean<Filter> mdcFilterRegistration() {
        FilterRegistrationBean<Filter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new MdcFilter());
        reg.setOrder(FilterOrder.MDC_SETUP_FILTER); // 높은 우선순위로 적용 (필요시 조정)
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
        reg.setOrder(FilterOrder.MDC_CLEAR_FILTER); // 항상 가장 나중에 실행되도록
        reg.addUrlPatterns("/*");
        return reg;
    }
}
