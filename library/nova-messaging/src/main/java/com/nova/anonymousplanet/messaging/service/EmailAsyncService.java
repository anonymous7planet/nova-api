package com.nova.anonymousplanet.messaging.service;

import com.nova.anonymousplanet.messaging.model.EmailPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.messaging.service
 * fileName : EmailAsyncService
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "nova.email", name = "enabled", havingValue = "true")
public class EmailAsyncService {

    private final EmailSenderService emailSenderService;


    @Async("emailExecutor")
    public void sendAsync(EmailPayload payload) {
        log.info("[Async] Start email: {}", payload.to());
        emailSenderService.send(payload);
        log.info("[Async] Complete email: {}", payload.to());
    }
}
