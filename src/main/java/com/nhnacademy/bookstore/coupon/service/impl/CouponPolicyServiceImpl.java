package com.nhnacademy.bookstore.coupon.service.impl;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import com.nhnacademy.bookstore.coupon.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {

    private final CouponPolicyRepository couponPolicyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CouponPolicyResponseDto> getAllCouponPolicies() {
        List<CouponPolicyResponseDto> responseDtos = couponPolicyRepository.findActivePolicy();

        if (responseDtos == null) {
            responseDtos = Collections.emptyList();
        }
        return responseDtos;
    }
}
