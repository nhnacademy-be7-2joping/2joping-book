package com.nhnacademy.bookstore.coupon.respository;import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import com.nhnacademy.bookstore.coupon.repository.coupon.CouponRepository;
import com.nhnacademy.bookstore.coupon.repository.coupon.impl.CouponRepositoryImpl;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * CouponRepositoryImplTest
 *
 * 이 클래스는 CouponRepositoryImpl의 findAllCoupons 메서드를 테스트합니다.
 * 쿠폰과 쿠폰 정책이 데이터베이스에 올바르게 저장되고 조회되는지 확인하는 테스트입니다.
 *
 * @since 1.0
 * author Luha
 */
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CouponRepositoryImplTest {

    @Autowired
    private CouponRepositoryImpl couponRepository;

    @Autowired
    private CouponRepository couponJpaRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        // 테스트를 위한 쿠폰 정책과 쿠폰 저장
        CouponPolicy policy = new CouponPolicy(null, "Test Policy", DiscountType.ACTUAL, 10, 100, 30, "Policy Detail", 50,false);
        couponPolicyRepository.save(policy);

        Coupon coupon = new Coupon(null, "Test Coupon", LocalDate.now(), LocalDate.now().plusDays(10), 100, policy, null);
        couponJpaRepository.save(coupon);
    }

    /**
     * findAllCoupons 메서드 테스트.
     *
     * Given: 데이터베이스에 쿠폰과 정책을 저장.
     * When: findAllCoupons 메서드를 사용하여 쿠폰 목록을 조회.
     * Then: 조회된 쿠폰 목록이 올바른지 확인.
     */
//    @Test
//    @Transactional
//    void testFindAllCoupons() {
//        // When: findAllCoupons 호출
//        List<CouponResponseDto> coupons = couponRepository.findAllCoupons();
//
//        // Then: 조회된 데이터 검증
//        assertNotNull(coupons);
//        assertEquals(1, coupons.size());
//        assertEquals("Test Coupon", coupons.get(0).name());
//        assertEquals("Test Policy", coupons.get(0).couponPolicyResponseDto().name());
//    }
}