package com.nhnacademy.bookstore.coupon.service;


import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.OrderCouponResponse;
import com.nhnacademy.bookstore.coupon.entity.Coupon;

import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import com.nhnacademy.bookstore.coupon.repository.coupon.CouponRepository;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponRepository;
import com.nhnacademy.bookstore.coupon.service.impl.MemberCouponServiceImpl;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * MemberCouponServiceImplTest
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

    @Mock
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 회원의 모든 쿠폰 조회 성공 테스트
     * Given: 특정 회원 ID.
     * When: getAllMemberCoupons 메서드 호출.
     * Then: 예상된 쿠폰 목록 반환.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("모든 회원 쿠폰 조회 - 성공")
    void getAllMemberCoupons_Success() {
        // given
        MemberCouponResponseDto responseDto = new MemberCouponResponseDto(1L, 2L, LocalDateTime.now(), LocalDateTime.now().plusDays(10), false, null, null);
        when(memberCouponRepository.getAllMemberCoupons(1L)).thenReturn(Collections.singletonList(responseDto));

        // when
        List<MemberCouponResponseDto> result = memberCouponService.getAllMemberCoupons(1L);

        // then
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().couponUsageId());
        assertEquals(2L, result.getFirst().couponId());
    }

    /**
     * 회원이 보유한 쿠폰이 없을 때 빈 목록 반환 테스트
     * Given: 특정 회원 ID.
     * When: getAllMemberCoupons 메서드 호출.
     * Then: 빈 리스트 반환.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("모든 회원 쿠폰 조회 - 빈 목록 반환")
    void getAllMemberCoupons_EmptyList() {
        // given
        when(memberCouponRepository.getAllMemberCoupons(1L)).thenReturn(Collections.emptyList());

        // when
        List<MemberCouponResponseDto> result = memberCouponService.getAllMemberCoupons(1L);

        // then
        assertTrue(result.isEmpty());
    }

    /**
     * 회원의 주문 가능한 쿠폰 조회 성공 테스트
     * Given: 특정 회원 ID.
     * When: getAllMemberOrderCoupons 메서드 호출.
     * Then: 예상된 주문 가능한 쿠폰 목록 반환.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("주문 가능한 쿠폰 조회 - 성공")
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
        assertEquals(1L, result.getFirst().couponUsageId());
        assertEquals("Test Coupon", result.getFirst().name());
        assertEquals(DiscountType.ACTUAL, result.getFirst().discountType());
        assertEquals(10, result.getFirst().discountValue());
        assertEquals("Policy Detail", result.getFirst().detail());
        assertEquals(100, result.getFirst().maxDiscount());
    }

    /**
     * 회원의 주문 가능한 쿠폰이 없을 때 빈 목록 반환 테스트
     * Given: 특정 회원 ID.
     * When: getAllMemberOrderCoupons 메서드 호출.
     * Then: 빈 리스트 반환.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("주문 가능한 쿠폰 조회 - 빈 목록 반환")
    void getAllMemberOrderCoupons_EmptyList() {
        // given
        when(memberCouponRepository.getAllMemberOrderCoupons(1L)).thenReturn(Collections.emptyList());

        // when
        List<OrderCouponResponse> result = memberCouponService.getAllMemberOrderCoupons(1L);

        // then
        assertTrue(result.isEmpty());
    }

    /**
     * WELCOME 쿠폰 저장 성공 테스트
     * Given: 회원 정보.
     * When: saveWelcomeCoupon 메서드 호출.
     * Then: WELCOME 쿠폰 저장 및 성공 반환.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("WELCOME 쿠폰 저장 - 성공")
    void saveMemberWelcomeCoupon_Success() {
        Member member = new Member("testLoginId", "Test Member", "nick", Gender.M, LocalDate.now(), 10, LocalDate.now(), null, false, 0, 0, null,
                null, null, null, null);
        CouponPolicy couponPolicy = new CouponPolicy(1L, "Test Policy",  DiscountType.PERCENT, 10, 100, 30, "Test Detail", 50, true);
        Coupon welcomeCoupon = new Coupon(1L, "11월 생일 쿠폰", LocalDate.now().minusDays(10), LocalDate.now().plusDays(10), 10, couponPolicy, null);
        MemberCoupon expectedMemberCoupon = new MemberCoupon(1L, welcomeCoupon, member, LocalDateTime.now(), LocalDateTime.now().plusDays(10), false, LocalDateTime.now());
        when(couponRepository.findByName("WELCOME 쿠폰")).thenReturn(welcomeCoupon);
        when(memberCouponRepository.save(any(MemberCoupon.class))).thenReturn(expectedMemberCoupon);

        boolean result = memberCouponService.saveWelcomeCoupon(member);

        assertTrue(result);
        verify(memberCouponRepository, times(1)).save(any(MemberCoupon.class));

    }

    /**
     * WELCOME 쿠폰 저장 실패 테스트
     * Given: WELCOME 쿠폰이 존재하지 않음.
     * When: saveWelcomeCoupon 메서드 호출.
     * Then: IllegalArgumentException 예외 발생.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("WELCOME 쿠폰 저장 - 실패")
    void saveMemberWelcomeCoupon_Fail() {
        // Given
        when(couponRepository.findByName("WELCOME 쿠폰")).thenReturn(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, this::executeSaveWelcomeCoupon);

        verify(couponRepository, times(1)).findByName("WELCOME 쿠폰");
        verify(memberCouponRepository, never()).save(any());
    }
    private void executeSaveWelcomeCoupon() {
        memberCouponService.saveWelcomeCoupon(new Member());
    }

    /**
     * 사용된 쿠폰 조회 성공 테스트
     * Given: 특정 회원 ID.
     * When: getAllMemberUsedCoupons 메서드 호출.
     * Then: 사용된 쿠폰 목록 반환.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("사용된 쿠폰 조회 - 성공")
    void getAllMemberUsedCoupons_Success() {
        // Given
        long customerId = 1L;

        // Mock 데이터 설정
        CouponResponseDto couponResponseDto = new CouponResponseDto(
                2L, // couponId
                "WELCOME 쿠폰", // 쿠폰 이름
                LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(10),
                10, null
                );

        List<MemberCouponResponseDto> expectedCoupons = List.of(
                new MemberCouponResponseDto(
                        1L, // couponUsageId
                        2L, // couponId
                        LocalDateTime.now().minusDays(15), // receiveTime
                        LocalDateTime.now().minusDays(5), // invalidTime
                        true, // isUsed
                        LocalDateTime.now().minusDays(10), // usedDate
                        couponResponseDto // CouponResponseDto
                )
        );

        when(memberCouponRepository.getExpiredOrUsedMemberCoupons(customerId)).thenReturn(expectedCoupons);

        // When
        List<MemberCouponResponseDto> result = memberCouponService.getAllMemberUsedCoupons(customerId);

        // Then
        assertEquals(1, result.size()); // 결과 리스트 크기 확인

        MemberCouponResponseDto coupon = result.getFirst();

        assertEquals(1L, coupon.couponUsageId());
        assertEquals(2L, coupon.couponId());
        assertEquals(true, coupon.isUsed());
        assertNotNull(coupon.receiveTime());
        assertNotNull(coupon.invalidTime());
        assertNotNull(coupon.usedDate());

        // CouponResponseDto 필드 검증
        assertNotNull(coupon.couponResponseDto());
        assertEquals(2L, coupon.couponResponseDto().couponId());
        assertEquals("WELCOME 쿠폰", coupon.couponResponseDto().name());

        // Verify
        verify(memberCouponRepository, times(1)).getExpiredOrUsedMemberCoupons(customerId);
    }
}