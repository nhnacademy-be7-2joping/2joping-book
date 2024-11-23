package com.nhnacademy.bookstore.coupon.service;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import com.nhnacademy.bookstore.coupon.service.impl.CouponPolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * CouponPolicyServiceImplTest
 * 이 클래스는 CouponPolicyServiceImpl의 비즈니스 로직을 테스트하여
 * 활성화된 쿠폰 정책 조회 기능의 동작을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
class CouponPolicyServiceImplTest {

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @InjectMocks
    private CouponPolicyServiceImpl couponPolicyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 활성화된 모든 쿠폰 정책 조회 성공 테스트
     * 활성화된 쿠폰 정책이 존재할 경우 예상된 데이터를 반환하는지 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    void getAllCouponPolicies_Success() {
        // given
        CouponPolicyResponseDto responseDto = new CouponPolicyResponseDto(1L, "Discount Policy", "PERCENTAGE", 10, 100, 30, "Policy Detail", 50);
        when(couponPolicyRepository.findActivePolicy()).thenReturn(Collections.singletonList(responseDto));

        // when
        List<CouponPolicyResponseDto> result = couponPolicyService.getAllCouponPolicies();

        // then
        assertEquals(1, result.size());
        assertEquals("Discount Policy", result.getFirst().name());
        assertEquals("PERCENTAGE", result.getFirst().discountType());
    }

    /**
     * 쿠폰 정책 조회 시 빈 목록 반환 테스트
     * 활성화된 쿠폰 정책이 없을 경우 빈 리스트가 반환되는지 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    void getAllCouponPolicies_EmptyList() {
        // given
        when(couponPolicyRepository.findActivePolicy()).thenReturn(Collections.emptyList());

        // when
        List<CouponPolicyResponseDto> result = couponPolicyService.getAllCouponPolicies();

        // then
        assertEquals(0, result.size());
    }
}