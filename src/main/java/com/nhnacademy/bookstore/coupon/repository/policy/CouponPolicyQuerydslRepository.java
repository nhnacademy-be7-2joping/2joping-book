package com.nhnacademy.bookstore.coupon.repository.policy;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;

import java.util.List;

public interface CouponPolicyQuerydslRepository {
    List<CouponPolicyResponseDto> findActivePolicy();
}
