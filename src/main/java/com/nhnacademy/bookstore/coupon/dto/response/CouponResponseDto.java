package com.nhnacademy.bookstore.coupon.dto.response;

import java.time.LocalDate;

public record CouponResponseDto(
        Long couponId,
        String name,
        LocalDate createdAt,
        LocalDate expiredAt,
        Integer quantity,
        CouponPolicyResponseDto couponPolicyResponseDto
) {
}
