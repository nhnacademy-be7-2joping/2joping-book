package com.nhnacademy.bookstore.coupon.service;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.OrderCouponResponse;
import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponRepository;
import com.nhnacademy.bookstore.coupon.service.impl.MemberCouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * MemberCouponServiceImplTest
 *
 * 이 클래스는 MemberCouponServiceImpl의 비즈니스 로직을 테스트하여 회원이 보유한 쿠폰 조회 기능의 동작을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
class MemberCouponServiceImplTest {

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @InjectMocks
    private MemberCouponServiceImpl memberCouponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 특정 회원의 모든 쿠폰 조회 성공 테스트
     * 회원 ID로 요청 시 예상된 쿠폰 목록이 반환되는지 확인합니다.
     */
    @Test
    void getAllMemberCoupons_Success() {
        // given
        MemberCouponResponseDto responseDto = new MemberCouponResponseDto(1L, 2L, LocalDateTime.now(), LocalDateTime.now().plusDays(10), false, null, null);
        when(memberCouponRepository.getAllMemberCoupons(1L)).thenReturn(Collections.singletonList(responseDto));

        // when
        List<MemberCouponResponseDto> result = memberCouponService.getAllMemberCoupons(1L);

        // then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).couponUsageId());
        assertEquals(2L, result.get(0).couponId());
    }

    /**
     * 회원이 보유한 쿠폰이 없을 때 빈 목록 반환 테스트
     * 회원 ID로 요청 시 보유한 쿠폰이 없을 경우 빈 리스트가 반환되는지 확인합니다.
     */
    @Test
    void getAllMemberCoupons_EmptyList() {
        // given
        when(memberCouponRepository.getAllMemberCoupons(Long.valueOf(1L))).thenReturn(Collections.emptyList());

        // when
        List<MemberCouponResponseDto> result = memberCouponService.getAllMemberCoupons(1L);

        // then
        assertTrue(result.isEmpty());
    }

    /**
     * 특정 회원의 주문 가능한 쿠폰 조회 성공 테스트
     * 회원 ID로 요청 시 예상된 주문 가능한 쿠폰 목록이 반환되는지 확인합니다.
     */
    @Test
    void getAllMemberOrderCoupons_Success() {
        // given
        OrderCouponResponse responseDto = new OrderCouponResponse(
                1L,
                "Test Coupon",
                LocalDateTime.now().plusDays(10),
                DiscountType.ACTUAL,
                10,
                3,
                "Policy Detail",
                100
        );

        when(memberCouponRepository.getAllMemberOrderCoupons(1L)).thenReturn(Collections.singletonList(responseDto));

        // when
        List<OrderCouponResponse> result = memberCouponService.getAllMemberOrderCoupons(1L);

        // then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).couponUsageId());
        assertEquals("Test Coupon", result.get(0).name());
        assertEquals(DiscountType.ACTUAL, result.get(0).discountType());
        assertEquals(10, result.get(0).discountValue());
        assertEquals("Policy Detail", result.get(0).detail());
        assertEquals(100, result.get(0).maxDiscount());
    }

    /**
     * 회원이 보유한 주문 가능한 쿠폰이 없을 때 빈 목록 반환 테스트
     * 회원 ID로 요청 시 주문 가능한 쿠폰이 없을 경우 빈 리스트가 반환되는지 확인합니다.
     */
    @Test
    void getAllMemberOrderCoupons_EmptyList() {
        // given
        when(memberCouponRepository.getAllMemberOrderCoupons(1L)).thenReturn(Collections.emptyList());

        // when
        List<OrderCouponResponse> result = memberCouponService.getAllMemberOrderCoupons(1L);

        // then
        assertTrue(result.isEmpty());
    }
}