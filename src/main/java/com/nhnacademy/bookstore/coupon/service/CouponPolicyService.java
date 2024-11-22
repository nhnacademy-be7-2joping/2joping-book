package com.nhnacademy.bookstore.coupon.service;


import com.nhnacademy.bookstore.coupon.dto.request.CreateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.request.UpdateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;

import java.util.List;

/**
 * CouponPolicyService
 * 이 인터페이스는 쿠폰 정책에 대한 비즈니스 로직을 정의합니다.
 * 활성화된 모든 쿠폰 정책을 조회하는 메서드를 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface CouponPolicyService {

    Long createCouponPolicy(CreateCouponPolicyRequest request);
    CouponPolicyResponseDto getCouponPolicy(Long couponPolicyId);
    List<CouponPolicyResponseDto> getAllCouponPolicies();
    Long updateCouponPolicy(Long id, UpdateCouponPolicyRequest request);
    void deleteCouponPolicy(Long id);
}
