package com.nhnacademy.bookstore.coupon.service;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;

import java.util.List;

public interface CouponPolicyService {
    List<CouponPolicyResponseDto> getAllCouponPolicies();
}
