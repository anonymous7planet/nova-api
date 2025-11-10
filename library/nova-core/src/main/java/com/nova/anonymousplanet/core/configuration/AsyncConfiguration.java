package com.nova.anonymousplanet.core.configuration;

import com.nova.anonymousplanet.core.handler.AsyncBaseExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    // 비동기 작업에 사용할 스레드 풀 설정
    @Override
    @Bean(name = "taskExecutor")  // 커스텀 스레드 풀을 @Bean으로 등록
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // 기본적으로 생성되는 스레드 수
        executor.setMaxPoolSize(50);   // 최대 스레드 수
        executor.setQueueCapacity(100); // 대기 큐의 용량
        executor.setThreadNamePrefix("Async-Executor-");  // 스레드 이름 접두사
        executor.initialize();  // 초기화
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncBaseExceptionHandler();
    }
}
