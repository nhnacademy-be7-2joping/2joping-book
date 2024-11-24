package com.nhnacademy.bookstore.coupon.dto.request;

import com.nhnacademy.bookstore.coupon.enums.DiscountType;

public record UpdateCouponPolicyRequest(

        String name,
        DiscountType discountType,
        Integer discountValue,
        Integer usageLimit,
        Integer duration,
        String detail,
        Integer maxDiscount,
        Boolean isActive
) {
}
