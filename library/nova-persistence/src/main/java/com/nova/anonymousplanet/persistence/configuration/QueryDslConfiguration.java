package com.nova.anonymousplanet.persistence.configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.common.entity.config
 * fileName : QueryDslConfiguration
 * author : Jinhong Min
 * date : 2025-05-07
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-05-07      Jinhong Min      최초 생성
 * ==============================================
 */
@Configuration
public class QueryDslConfiguration {

    private final EntityManager entityManager;

    public QueryDslConfiguration(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
