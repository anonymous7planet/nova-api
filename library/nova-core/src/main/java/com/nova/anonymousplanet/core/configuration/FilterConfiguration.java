package com.nova.anonymousplanet.core.configuration;

import com.nova.anonymousplanet.core.filter.TraceAndRequestIdFilter;
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
@Configuration
public class FilterConfiguration {
    @Bean
    public FilterRegistrationBean<TraceAndRequestIdFilter> traceFilterRegistration() {
        FilterRegistrationBean<TraceAndRequestIdFilter> filter = new FilterRegistrationBean<>(new TraceAndRequestIdFilter());
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE); // 서비스 내에서 최우선으로 적용
        return filter;
    }
}
