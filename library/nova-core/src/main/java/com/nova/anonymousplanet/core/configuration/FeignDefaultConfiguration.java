package com.nova.anonymousplanet.core.configuration;

import feign.Logger;
import feign.Retryer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.configuration
 * fileName : FeignDefaultConfiguration
 * author : Jinhong Min
 * date : 2026-03-15
 * description :
 * Feign Client 전역 공통 설정
 * 각 서비스의 ClientEntity에서 configuration으로 참조하여 사용
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-15      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
public class FeignDefaultConfiguration {

    /**
     * Feign 로깅 레벨 설정
     * FULL: Header, Body, Metadata를 모두 로깅 (개발 단계에서 유용)
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 재시도 전략 설정
     * 기본적으로는 재시도하지 않도록 설정 (안정성을 위해 필요 시 서비스별 별도 설정 권장)
     */
    @Bean
    @ConditionalOnMissingBean
    public Retryer retryer() {
        // 100ms 간격으로 시작하여 최대 1s 간격으로 3번 재시도
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1L), 3);
    }

    /*
     * 타임아웃 설정 (Connect: 5초, Read: 10초)
     * 서비스 성격에 따라 각 Client별로 커스텀 가능
     */
    // @Bean
    // public Request.Options options() {
    //     return new Request.Options(5, TimeUnit.SECONDS, 10, TimeUnit.SECONDS, true);
    // }

}
