package com.nhnacademy.bookstore.coupon.service;


import com.nhnacademy.bookstore.common.error.exception.coupon.CouponPolicyNotFoundException;
import com.nhnacademy.bookstore.coupon.dto.request.CreateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.request.UpdateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import com.nhnacademy.bookstore.coupon.service.impl.CouponPolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
     * 쿠폰 정책 조회 시 빈 목록 반환 테스트
     * 활성화된 쿠폰 정책이 없을 경우 빈 리스트가 반환되는지 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("활성화된 쿠폰 정책 조회 - 빈 목록 반환")
    void getAllCouponPolicies_EmptyList() {
        // given
        when(couponPolicyRepository.findActivePolicy()).thenReturn(Collections.emptyList());

        // when
        List<CouponPolicyResponseDto> result = couponPolicyService.getAllCouponPolicies();

        // then
        assertEquals(0, result.size());
    }

    /**
     * 새로운 쿠폰 정책 생성 성공 테스트.
     * Given: 새로운 쿠폰 정책 생성 요청.
     * When: createCouponPolicy 호출.
     * Then: 생성된 쿠폰 정책 ID 반환.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 생성 - 성공")
    void createCouponPolicy_Success() {
        // Given
        CreateCouponPolicyRequest request = new CreateCouponPolicyRequest(
                "Test Policy", DiscountType.PERCENT, 10, 100, 30, "Test Detail", 50, true);
        CouponPolicy couponPolicy = new CouponPolicy(1L, request.name(), request.discountType(), request.discountValue(), request.usageLimit(),
                request.duration(), request.detail(), request.maxDiscount(), request.isActive());

        when(couponPolicyRepository.save(any(CouponPolicy.class))).thenReturn(couponPolicy);

        // When
        Long result = couponPolicyService.createCouponPolicy(request);

        // Then
        assertNotNull(result);
        assertEquals(1L, result);
    }

    /**
     * 쿠폰 정책 조회 성공 테스트.
     * Given: 저장된 쿠폰 정책이 존재.
     * When: getCouponPolicy 호출.
     * Then: 조회된 쿠폰 정책 반환.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 조회 - 성공")
    void getCouponPolicy_Found() {
        // Given
        CouponPolicy couponPolicy = new CouponPolicy(1L, "Test Policy",  DiscountType.PERCENT, 10, 100, 30, "Test Detail", 50, true);

        when(couponPolicyRepository.findByCouponPolicyId(1L)).thenReturn(Optional.of(couponPolicy));

        // When
        CouponPolicyResponseDto result = couponPolicyService.getCouponPolicy(1L);

        // Then
        assertNotNull(result);
        assertEquals("Test Policy", result.name());
    }

    /**
     * 쿠폰 정책 조회 실패 테스트.
     * Given: 저장된 쿠폰 정책이 없음.
     * When: getCouponPolicy 호출.
     * Then: 예외 발생.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 조회 - 실패")
    void getCouponPolicy_NotFound() {
        // Given
        when(couponPolicyRepository.findByCouponPolicyId(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> couponPolicyService.getCouponPolicy(1L));
        assertTrue(exception.getMessage().contains("쿠폰 정책을 찾을 수 없습니다."));
    }

    /**
     * 활성화된 쿠폰 정책 목록 조회 테스트.
     * Given: 활성화된 쿠폰 정책 존재.
     * When: getAllCouponPolicies 호출.
     * Then: 활성화된 쿠폰 정책 목록 반환.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("활성화된 쿠폰 정책 목록 조회 - 성공")
    void getAllCouponPolicies_WithResults() {
        // Given
        CouponPolicy couponPolicy = new CouponPolicy(1L, "Test Policy",  DiscountType.PERCENT, 10, 100, 30, "Test Detail", 50, true);


        when(couponPolicyRepository.findByIsActiveTrue()).thenReturn(List.of(couponPolicy));

        // When
        List<CouponPolicyResponseDto> result = couponPolicyService.getAllCouponPolicies();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Policy", result.getFirst().name());
    }

    /**
     * 쿠폰 정책 업데이트 성공 테스트.
     * Given: 기존 쿠폰 정책이 존재.
     * When: updateCouponPolicy 호출.
     * Then: 업데이트된 쿠폰 정책 반환.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 수정 - 성공")
    void updateCouponPolicy_Success() {
        // Given

        CouponPolicy couponPolicy = new CouponPolicy(1L, "Old Policy",  DiscountType.PERCENT, 10, 100, 30, "Old Detail", 50, true);


        UpdateCouponPolicyRequest request = new UpdateCouponPolicyRequest(
                "Updated Policy", DiscountType.ACTUAL, 20, 200, 60, "Updated Detail", 100, false);

        when(couponPolicyRepository.findByCouponPolicyId(1L)).thenReturn(Optional.of(couponPolicy));
        when(couponPolicyRepository.save(couponPolicy)).thenReturn(couponPolicy);

        // When
        Long result = couponPolicyService.updateCouponPolicy(1L, request);

        // Then
        assertNotNull(result);
        assertEquals(1L, result);
        assertEquals("Updated Policy", couponPolicy.getName());
    }

    /**
     * 쿠폰 정책 비활성화 성공 테스트.
     * Given: 기존 쿠폰 정책이 활성 상태.
     * When: deleteCouponPolicy 호출.
     * Then: 쿠폰 정책이 비활성 상태로 변경.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 비활성화 - 성공")
    void deleteCouponPolicy_Success() {
        // Given
        CouponPolicy couponPolicy = new CouponPolicy(1L, "Test Policy",  DiscountType.PERCENT, 10, 100, 30, "Test Detail", 50, true);


        when(couponPolicyRepository.findByCouponPolicyId(1L)).thenReturn(Optional.of(couponPolicy));

        // When
        couponPolicyService.deleteCouponPolicy(1L);

        // Then
        assertFalse(couponPolicy.getIsActive());
    }
    /**
     * 테스트: 쿠폰 정책이 존재하지 않을 경우 예외 발생
     * 예상 결과: CouponPolicyNotFoundException 발생
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 수정 시 존재하지 않는 정책 - CouponPolicyNotFoundException 발생")
    void testUpdateCouponPolicy_NotFound() {
        // given
        Long invalidPolicyId = 999L;
        UpdateCouponPolicyRequest request = new UpdateCouponPolicyRequest(
                "New Name", DiscountType.PERCENT, 10, 100, 30, "Details", 50, true
        );

        when(couponPolicyRepository.findByCouponPolicyId(invalidPolicyId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                CouponPolicyNotFoundException.class,
                () -> couponPolicyService.updateCouponPolicy(invalidPolicyId, request),
                "쿠폰 정책을 찾을 수 없습니다."
        );
    }

    /**
     * 테스트: 삭제하려는 쿠폰 정책이 존재하지 않을 경우 예외 발생
     * 예상 결과: CouponPolicyNotFoundException 발생
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 삭제 시 존재하지 않는 정책 - CouponPolicyNotFoundException 발생")
    void testDeleteCouponPolicy_NotFound() {
        // given
        Long invalidPolicyId = 999L;

        when(couponPolicyRepository.findByCouponPolicyId(invalidPolicyId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                CouponPolicyNotFoundException.class,
                () -> couponPolicyService.deleteCouponPolicy(invalidPolicyId),
                "쿠폰 정책을 찾을 수 없습니다."
        );
    }
}