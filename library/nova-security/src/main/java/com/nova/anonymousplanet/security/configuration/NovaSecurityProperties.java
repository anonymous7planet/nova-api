package com.nova.anonymousplanet.security.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.configuration
 * fileName : NovaSecurityProperties
 * author : Jinhong Min
 * date : 2026-02-02
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-02      Jinhong Min      최초 생성
 * ==============================================
 */


@ConfigurationProperties(prefix = "nova.security")
public record NovaSecurityProperties (
        String xGatewaySecret,
        List<String> serviceWhiteList
){
    public NovaSecurityProperties {
        if(serviceWhiteList == null) {
            serviceWhiteList = new ArrayList<>();
        }
    }
}
