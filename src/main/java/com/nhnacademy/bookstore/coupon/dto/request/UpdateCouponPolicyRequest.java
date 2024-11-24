package com.nhnacademy.bookstore.coupon.dto.request;

public record UpdateCouponPolicyRequest(

        String name,
        Integer discountValue,
        Integer usageLimit,
        Integer duration,
        String detail,
        Integer maxDiscount
) {
}
