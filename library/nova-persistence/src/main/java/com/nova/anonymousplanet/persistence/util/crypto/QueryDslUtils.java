package com.nova.anonymousplanet.persistence.util.crypto;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;

import java.util.Set;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.persistence.util.crypto
 * fileName : QueryDslUtils
 * author : Jinhong Min
 * date : 2026-02-23
 * description :
 * QueryDSL 동적 정렬 및 보안 유틸리티
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-23      Jinhong Min      최초 생성
 * ==============================================
 */
public class QueryDslUtils {
    /**
     * @param entityClass 엔티티 클래스
     * @param variable    Q클래스 변수명
     * @param property    정렬 필드명
     * @param isAsc       오름차순 여부
     * @param allowed     화이트리스트
     * @return 제네릭이 적용된 OrderSpecifier
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static OrderSpecifier<?> getSortedOrder(Class<?> entityClass, String variable, String property, boolean isAsc, Set<String> allowed) {

        // 1. SQL Injection 방어
        if (!allowed.contains(property)) {
            throw new IllegalArgumentException("Invalid sort property: " + property);
        }

        // 2. PathBuilder 생성
        PathBuilder<?> pathBuilder = new PathBuilder<>(entityClass, variable);

        // 3. 정렬 방향 결정
        Order order = isAsc ? Order.ASC : Order.DESC;

        // 4. 필드 경로 추출
        // ComparableExpressionBase로 받아야 정렬(OrderSpecifier)에 주입 가능합니다.
        ComparableExpressionBase<?> path = pathBuilder.getComparable(property, Comparable.class);

        // 5. 타입 안정성을 갖춘 OrderSpecifier 반환
        return new OrderSpecifier(order, path);
    }
}