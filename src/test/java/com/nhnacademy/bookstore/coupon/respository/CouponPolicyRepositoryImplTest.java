package com.nhnacademy.bookstore.coupon.respository;


import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * CouponPolicyRepositoryImplTest
 * 이 클래스는 CouponPolicyRepositoryImpl의 findActivePolicy 메서드를 테스트합니다.
 * 활성화된 쿠폰 정책이 데이터베이스에 올바르게 저장되고 조회되는지 확인하는 테스트입니다.
 *
 * @since 1.0
 * author Luha
 */
@DataJpaTest
@Import(QuerydslConfig.class) // QueryDSL 설정 추가
@ActiveProfiles("test") // 테스트 환경 적용
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CouponPolicyRepositoryImplTest {

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    /**
     * 데이터 초기화
     * 활성/비활성 쿠폰 정책 데이터를 데이터베이스에 저장합니다.
     */
    @BeforeEach
    void setUp() {
        // Given: 데이터 초기화
        CouponPolicy activePolicy = new CouponPolicy(
                null, "Active Policy", DiscountType.ACTUAL, 20, 5, 30,
                "Test active policy", 50, true
        );
        CouponPolicy inactivePolicy = new CouponPolicy(
                null, "Inactive Policy", DiscountType.ACTUAL, 10, 10, 60,
                "Test inactive policy", 100, false
        );

        couponPolicyRepository.save(activePolicy);
        couponPolicyRepository.save(inactivePolicy);
    }

    /**
     * 테스트: 활성화된 쿠폰 정책 조회
     * Given: 활성화된 정책이 데이터베이스에 저장되어 있음.
     * When: findActivePolicy 메서드를 호출하여 활성화된 정책을 조회.
     * Then: 반환된 목록에 활성화된 정책만 포함되었는지 검증.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("활성화된 쿠폰 정책 조회 - 성공")
    void testFindActivePolicy() {
        // When
        List<CouponPolicyResponseDto> activePolicies = couponPolicyRepository.findActivePolicy();

        // Then
        assertThat(activePolicies).isNotEmpty();
        assertThat(activePolicies).hasSize(1);
        assertThat(activePolicies.getFirst().name()).isEqualTo("Active Policy");
        assertThat(activePolicies.getFirst().detail()).isEqualTo("Test active policy");
    }
}