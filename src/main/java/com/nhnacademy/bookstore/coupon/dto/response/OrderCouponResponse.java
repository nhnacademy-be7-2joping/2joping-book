package com.nhnacademy.bookstore.coupon.dto.response;

import com.nhnacademy.bookstore.coupon.enums.DiscountType;

import java.time.LocalDateTime;

public record OrderCouponResponse(

        Long couponUsageId,
        String name,
        LocalDateTime invalidTime,
        DiscountType discountType,
        int discountValue,
        int usageLimit,
        String detail,
        int maxDiscount
        ) {
}
