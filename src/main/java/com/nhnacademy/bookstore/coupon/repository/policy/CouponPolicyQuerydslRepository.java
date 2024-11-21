package com.nhnacademy.bookstore.coupon.repository.policy;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;

import java.util.List;

/**
 * CouponPolicyQuerydslRepository
 * 이 인터페이스는 Querydsl을 사용하여 쿠폰 정책을 조회하기 위한 커스텀 레포지토리입니다.
 * 활성화된 쿠폰 정책을 조회하는 메서드를 정의합니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface CouponPolicyQuerydslRepository {
    List<CouponPolicyResponseDto> findActivePolicy();
}
