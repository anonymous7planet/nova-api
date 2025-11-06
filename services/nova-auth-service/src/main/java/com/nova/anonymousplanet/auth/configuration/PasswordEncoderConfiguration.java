package com.nova.anonymousplanet.auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.configuration
  fileName : PasswordEncoder
  author : Jinhong Min
  date : 2025-10-28
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-28      Jinhong Min      최초 생성
  ==============================================
 */
@Configuration
public class PasswordEncoderConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
