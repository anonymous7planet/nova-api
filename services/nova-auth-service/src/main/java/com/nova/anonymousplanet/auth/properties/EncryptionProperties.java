package com.nova.anonymousplanet.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.properties
 * fileName : EncryptionProperties
 * author : Jinhong Min
 * date : 2026-02-03
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-03      Jinhong Min      최초 생성
 * ==============================================
 */
@ConfigurationProperties(prefix = "nova.secret.encryption")
public record EncryptionProperties(
        String key,
        String salt
) {
}
