package com.nhnacademy.bookstore.coupon.service.impl;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import com.nhnacademy.bookstore.coupon.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


/**
 * CouponPolicyServiceImpl
 *
 * 이 클래스는 쿠폰 정책에 대한 비즈니스 로직을 구현하는 서비스 클래스입니다.
 * 활성화된 모든 쿠폰 정책을 조회하는 기능을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
@Service
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {

    private final CouponPolicyRepository couponPolicyRepository;

    /**
     * 활성화된 모든 쿠폰 정책을 조회하여 CouponPolicyResponseDto 목록으로 반환합니다.
     * 조회 결과가 없을 경우 빈 리스트를 반환합니다.
     *
     * @return 활성 쿠폰 정책 정보를 담은 CouponPolicyResponseDto 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CouponPolicyResponseDto> getAllCouponPolicies() {
        List<CouponPolicyResponseDto> responseDtos = couponPolicyRepository.findActivePolicy();

        if (responseDtos.isEmpty()) {
            responseDtos = Collections.emptyList();
        }
        return responseDtos;
    }
}
