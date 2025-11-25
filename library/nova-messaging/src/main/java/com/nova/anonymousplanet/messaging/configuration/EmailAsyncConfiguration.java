package com.nova.anonymousplanet.messaging.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.configuration.sender.email
 * fileName : EmailAsyncConfiguration
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * 비동기 이메일 전송을 위한 스레드풀 설정
 * 필요한 경우 어플리케이션 설정에서 core/max/queue를 override 하세요.
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */

@EnableAsync
@Configuration
@ConditionalOnProperty(prefix = "nova.email", name = "enabled", havingValue = "true")
public class EmailAsyncConfiguration {

    @Bean("emailExecutor")
    public Executor emailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // 기본 동시 작업 수
        executor.setMaxPoolSize(10); // 최대 스레드 수
        executor.setQueueCapacity(200); // 큐 사이즈
        executor.setThreadNamePrefix("email-async-");
        executor.initialize();
        return executor;
    }
}
