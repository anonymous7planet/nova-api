package com.nova.anonymousplanet.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 설정 파일의 우선 순위 오른쪽으로 갈수록 우선 순위가 높다
 * 프로젝트의 application.yml과 프로젝트의 application-{환경}.yml파일의 같은 내용이 있다면 application-{환경}.yml에 내용이 적용된다
 * 프로젝트의 application.yml < 설정 저장소의 application.yml < 프로젝트의 application-{환경}.yml < 설정 저장소의 application-{환경}.yml
 */
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}