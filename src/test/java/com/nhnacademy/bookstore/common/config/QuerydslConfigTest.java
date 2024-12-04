package com.nhnacademy.bookstore.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * QuerydslConfigTest 클래스는 QuerydslConfig 클래스의 동작을 검증하기 위한 테스트 클래스입니다.
 * 주요 테스트 항목:
 * - JPAQueryFactory Bean이 EntityManager를 사용하여 올바르게 생성되는지 확인
 * 사용된 기술:
 * - Mockito를 사용하여 EntityManager를 모킹
 * - JUnit 5 및 AssertJ를 통해 테스트 검증
 *
 * @author Luha
 * @version 1.0
 */
class QuerydslConfigTest {

    @Mock
    private EntityManager entityManager;

    /**
     * 테스트: JPAQueryFactory Bean 생성 확인
     * 예상 결과: JPAQueryFactory 객체가 EntityManager를 사용하여 생성된다.
     */
    @Test
    @DisplayName("JPAQueryFactory Bean 생성 테스트")
    void testJpaQueryFactoryBeanCreation() {
        // given
        QuerydslConfig querydslConfig = new QuerydslConfig();

        // when
        JPAQueryFactory jpaQueryFactory = querydslConfig.jpaQueryFactory(entityManager);

        // then
        assertThat(jpaQueryFactory).isNotNull();
    }
}