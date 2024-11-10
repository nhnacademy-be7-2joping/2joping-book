package com.nhnacademy.bookstore.coupon.dto.request;


import java.time.LocalDate;

public record CouponRequestDto(
        Long couponPolicyId,
        String name,
        LocalDate expiredAt,
        Integer quantity
) {
    public CouponRequestDto(Long couponPolicyId,
                            String name,
                            LocalDate expiredAt) {
        this(couponPolicyId, name, expiredAt, null);
    }
}
