package com.nhnacademy.bookstore.coupon.dto.response;

public record CouponPolicyResponseDto(
        Long couponPolicyId,
        String name,
        String discountType,
        Integer discountValue,
        Integer usageLimit,
        Integer duration,
        String detail,
        Integer maxDiscount
) {
}
