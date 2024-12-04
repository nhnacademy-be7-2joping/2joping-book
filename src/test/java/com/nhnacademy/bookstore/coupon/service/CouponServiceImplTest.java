package com.nhnacademy.bookstore.coupon.service;


import com.nhnacademy.bookstore.common.error.exception.coupon.CouponPolicyNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.coupon.DuplicateCouponNameException;
import com.nhnacademy.bookstore.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.mapper.CouponMapper;
import com.nhnacademy.bookstore.coupon.repository.coupon.CouponRepository;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import com.nhnacademy.bookstore.coupon.service.impl.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * CouponServiceImplTest
 * 이 클래스는 CouponServiceImpl의 비즈니스 로직을 테스트하여 쿠폰 생성과 조회 기능을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 쿠폰 생성 성공 테스트
     * 새로운 쿠폰을 생성하고 예상된 응답 DTO가 반환되는지 확인합니다.
     */
    @Test
    void createCoupon_Success() {
        // given
        CouponRequestDto requestDto = new CouponRequestDto(1L, "New Coupon", LocalDate.now());
        CouponPolicy couponPolicy = new CouponPolicy();
        Coupon coupon = new Coupon();
        CouponResponseDto responseDto = new CouponResponseDto(1L, "New Coupon", LocalDate.now(), LocalDate.now().plusDays(30), 10, null);

        when(couponRepository.existsByName(requestDto.name())).thenReturn(false);
        when(couponPolicyRepository.findById(requestDto.couponPolicyId())).thenReturn(Optional.of(couponPolicy));
        when(couponMapper.toCouponResponseDto(any(Coupon.class))).thenReturn(responseDto);
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        // when
        CouponResponseDto result = couponService.create(requestDto);

        // then
        assertEquals(responseDto, result);
        verify(couponRepository).save(any(Coupon.class));
    }

    /**
     * 쿠폰 생성 시 중복 이름으로 인한 예외 발생 테스트
     * 이미 존재하는 쿠폰 이름으로 생성 요청 시 DuplicateCouponNameException이 발생하는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    void createCoupon_DuplicateNameException() {
        // given
        CouponRequestDto requestDto = new CouponRequestDto(1L, "Existing Coupon", LocalDate.now());

        when(couponRepository.existsByName(requestDto.name())).thenReturn(true);

        // when & then
        DuplicateCouponNameException exception = assertThrows(DuplicateCouponNameException.class, () -> couponService.create(requestDto));
        assertEquals("쿠폰 이름이 이미 존재합니다:Existing Coupon", exception.getMessage());
    }

    /**
     * 모든 쿠폰 조회 성공 테스트
     * 저장된 모든 쿠폰을 조회하고 예상된 쿠폰 목록이 반환되는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    void getAllCoupons_Success() {
        // given
        CouponResponseDto responseDto = new CouponResponseDto(1L, "Sample Coupon", LocalDate.now(), LocalDate.now().plusDays(10), 5, null);
        when(couponRepository.findAllCoupons()).thenReturn(Collections.singletonList(responseDto));

        // when
        List<CouponResponseDto> result = couponService.getAllCouponse();

        // then
        assertEquals(1, result.size());
        assertEquals("Sample Coupon", result.getFirst().name());
    }

    /**
     * 모든 쿠폰 조회 시 빈 목록 반환 테스트
     * 저장된 쿠폰이 없을 경우 빈 리스트가 반환되는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    void getAllCoupons_EmptyList() {
        // given
        when(couponRepository.findAllCoupons()).thenReturn(Collections.emptyList());

        // when
        List<CouponResponseDto> result = couponService.getAllCouponse();

        // then
        assertTrue(result.isEmpty());
    }
    /**
     * 쿠폰 생성 시 존재하지 않는 쿠폰 정책으로 인한 예외 발생 테스트
     * 존재하지 않는 쿠폰 정책 ID로 요청 시 CouponPolicyNotFoundException이 발생하는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    void createCoupon_CouponPolicyNotFoundException() {
        // given
        CouponRequestDto requestDto = new CouponRequestDto(999L, "New Coupon", LocalDate.now()); // 없는 정책 ID
        when(couponRepository.existsByName(requestDto.name())).thenReturn(false);
        when(couponPolicyRepository.findById(requestDto.couponPolicyId())).thenReturn(Optional.empty()); // 정책이 없음

        // when & then
        CouponPolicyNotFoundException exception = assertThrows(CouponPolicyNotFoundException.class, () -> couponService.create(requestDto));
        assertEquals("존재하지 않는 쿠폰 정책입니다: 999", exception.getMessage());
    }
}