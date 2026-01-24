package com.nova.anonymousplanet.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * projectName : Default (Template) Project
 * packageName : com.nova.anonymousplanet
 * fileName : ${NAME}
 * author : Jinhong Min
 * date : 2026-01-11
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-11      Jinhong Min      최초 생성
 * ==============================================
 */
@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan(value = "com.nova.anonymousplanet")
@EntityScan(value = "com.nova.anonymousplanet") // entity 스캔 범위 설정
@SpringBootApplication
public class SystemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemServiceApplication.class, args);
    }
}