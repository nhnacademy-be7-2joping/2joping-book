package com.nhnacademy.bookstore.coupon.respository;


import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import com.nhnacademy.bookstore.coupon.repository.coupon.CouponRepository;
import com.nhnacademy.bookstore.coupon.repository.coupon.impl.CouponRepositoryImpl;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CouponRepositoryImplTest
 * 이 클래스는 CouponRepositoryImpl의 findAllCoupons 메서드를 테스트합니다.
 * 쿠폰 및 쿠폰 정책 데이터를 저장하고 조회 기능의 동작을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
@DataJpaTest
@Import(QuerydslConfig.class) // QueryDSL 설정 포함
@ActiveProfiles("test") // 테스트 프로필 활성화
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CouponRepositoryImplTest {

    @Autowired
    private CouponRepositoryImpl couponRepository;

    @Autowired
    private CouponRepository couponJpaRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    /**
     * 데이터 초기화
     * 테스트용 쿠폰 정책과 쿠폰을 데이터베이스에 저장합니다.
     */
    @BeforeEach
    void setUp() {
        // 테스트를 위한 쿠폰 정책과 쿠폰 저장
        CouponPolicy policy = new CouponPolicy(null, "Test Policy", DiscountType.ACTUAL, 10, 100, 30, "Policy Detail", 50, false);
        couponPolicyRepository.save(policy);

        Coupon coupon = new Coupon(null, "Test Coupon", LocalDate.now(), LocalDate.now().plusDays(10), 100, policy, null);
        couponJpaRepository.save(coupon);
    }


    /**
     * 테스트: 모든 쿠폰 조회
     * Given: 쿠폰이 데이터베이스에 저장되어 있음.
     * When: findAllCoupons 메서드를 호출.
     * Then: 저장된 쿠폰의 이름과 정책이 올바르게 조회되는지 검증.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("모든 쿠폰 조회 - 성공")
    void testFindAllCoupons() {
        // When: findAllCoupons 호출
        List<CouponResponseDto> coupons = couponRepository.findAllCoupons();

        // Then: 조회된 데이터 검증
        assertThat(coupons).hasSize(1); // 저장한 쿠폰이 하나 있어야 함
        assertThat(coupons.getFirst().name()).isEqualTo("Test Coupon"); // 쿠폰 이름 검증
        assertThat(coupons.getFirst().couponPolicyResponseDto().name()).isEqualTo("Test Policy"); // 정책 이름 검증
    }
}