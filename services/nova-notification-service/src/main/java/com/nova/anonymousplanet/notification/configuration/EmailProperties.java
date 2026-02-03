package com.nova.anonymousplanet.notification.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.configuration.sender.email
 * fileName : EmailProperties
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
@ConfigurationProperties(prefix="nova.email")
@Getter
@Setter
public class EmailProperties {

    // 모듈 활성화 여부(기본 false)
    private boolean enable = false;

    // 발송자 주소
    private String from = "anonoymous7planet@noreply.com";

    private String externalImagePath = "";
    // 외부 템플릿 디렉토리(파일 시스템 경로), 비어있으면 classpath에서 찾음
    private String externalTemplatePath = "";
    private String suffix = ".html";

    // 첨부파일 최대 크기(바이트) 기본 10MB
    private long maxAttachmentSize = 10* 1024 * 1024L;

    // SMTP 설정
    private String host = "smtp.gmail.com";
    private int port = 587;
    private String username = "anonymous7planet@gmail.com";
    private String password = "bhuw fbxc elis hpte";
}
