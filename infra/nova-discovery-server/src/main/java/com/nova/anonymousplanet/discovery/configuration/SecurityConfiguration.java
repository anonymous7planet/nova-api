package com.nova.anonymousplanet.discovery.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf disabled
        http.csrf(AbstractHttpConfigurer::disable);

        // 모든 경로에 대해 권한이 필요하게 한다
        http.authorizeHttpRequests((auth) -> auth.anyRequest().authenticated());

        // 로그인 방식은 http 베이직 방식
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    // 서버에 접속할 수 있는 비밀번호와 아이디 등록하기(인메모리 타입)
    @Bean
    public UserDetailsService userDetailsService() {

        // 사용자 생성
        UserDetails admin = User.builder()
                .username("admin")
                .password(bCryptPasswordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
