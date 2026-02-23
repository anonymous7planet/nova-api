package com.nova.anonymousplanet.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.anonymousplanet.security.xss.XssRaw;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.security.configuration
 * fileName : InternalFeignConfig
 * author : Jinhong Min
 * date : 2026-02-23
 * description :
 * 서비스 간 호출 시에는 원본 데이터를 유지하도록 @XssRaw 기반의 Encoder/Decoder 사용
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-23      Jinhong Min      최초 생성
 * ==============================================
 */
public class InternalFeignConfiguration {

    @Bean
    public Encoder feignEncoder(@Qualifier("rawObjectMapper") ObjectMapper rawMapper) {
        return new SpringEncoder(() -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter(rawMapper)));
    }

    @Bean
    public Decoder feignDecoder(@Qualifier("rawObjectMapper") ObjectMapper rawMapper) {
        return new ResponseEntityDecoder(new SpringDecoder(() -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter(rawMapper))));
    }
}
