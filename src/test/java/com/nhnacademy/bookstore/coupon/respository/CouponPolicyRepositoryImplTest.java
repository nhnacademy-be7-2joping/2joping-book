package com.nhnacademy.bookstore.coupon.respository;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CouponPolicyRepositoryImplTest
 * 이 클래스는 CouponPolicyRepositoryImpl의 findActivePolicy 메서드를 테스트합니다.
 * 활성화된 쿠폰 정책이 데이터베이스에 올바르게 저장되고 조회되는지 확인하는 테스트입니다.
 *
 * @since 1.0
 * author Luha
 */
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CouponPolicyRepositoryImplTest {


    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        // 활성 쿠폰 정책 데이터 준비
        CouponPolicy activePolicy = new CouponPolicy(null, "Active Policy", DiscountType.ACTUAL, 20, 5, 30, "Test active policy", 50, true);
        CouponPolicy inactivePolicy = new CouponPolicy(null, "Inactive Policy", DiscountType.ACTUAL, 10, 10, 60, "Test inactive policy", 100, false);

        couponPolicyRepository.save(activePolicy);
        couponPolicyRepository.save(inactivePolicy);
    }

    /**
     * findActivePolicy 메서드 테스트.
     * Given: 활성화된 쿠폰 정책을 저장.
     * When: findActivePolicy 메서드를 사용하여 정책 목록을 조회.
     * Then: 조회된 정책 목록이 올바른지 확인.
     */
    @Test
    @Transactional
    void testFindActivePolicy() {
        // When: findActivePolicy 호출
        List<CouponPolicyResponseDto> activePolicies = couponPolicyRepository.findActivePolicy();

        // Then: 조회된 데이터 검증
        assertFalse(activePolicies.isEmpty());
        assertEquals("생일 쿠폰 정책", activePolicies.getFirst().name());
        assertEquals("11월 생일 쿠폰", activePolicies.getFirst().detail());
    }
}