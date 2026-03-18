package com.nova.anonymousplanet.core.configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.configuration
 * fileName : PropertyEncryptConfiguration
 * author : Jinhong Min
 * date : 2026-03-18
 * description :
 * Project Nova: 전사 공통 암호화(Jasypt) 설정
 * 패키지: com.nova.library.security.config.jasypt
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-03-18      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
@EnableEncryptableProperties // Spring은 application.yml이나 bootstrap.yml을 읽을 때 ENC( ... )로 감싸진 값이 있는지 전수 조사
public class PropertyEncryptConfiguration {

    @Value("${jasypt.encryptor.password}")
    private String encryptKey;

    @Bean("jasyptStringEncryptor")
    @ConditionalOnMissingBean(name = "jasyptStringEncryptor") // jasyptStringEncryptor이 없을경우 bean생성
    public StringEncryptor jasyptStringEncryptor() {
        // PooledPaddedStringEncryptor 대신 PooledPBEStringEncryptor 사용
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(encryptKey);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");

        encryptor.setConfig(config);
        return encryptor;
    }
}
