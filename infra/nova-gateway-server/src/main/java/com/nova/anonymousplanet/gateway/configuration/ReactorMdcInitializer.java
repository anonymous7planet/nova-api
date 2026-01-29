package com.nova.anonymousplanet.gateway.configuration;

import com.nova.anonymousplanet.gateway.logging.ReactorMdcHook;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.gateway.configuration
 * fileName : ReactorMdcInitializer
 * author : Jinhong Min
 * date : 2026-01-27
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-27      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
public class ReactorMdcInitializer {

    @PostConstruct
    public void init() {
        ReactorMdcHook.install();
    }
}
