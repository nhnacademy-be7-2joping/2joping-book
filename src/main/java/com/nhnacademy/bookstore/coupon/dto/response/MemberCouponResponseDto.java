package com.nhnacademy.bookstore.coupon.dto.response;

import java.time.LocalDateTime;

public record MemberCouponResponseDto(

        Long couponUsageId,

        Long couponId,

        LocalDateTime receiveTime,

        LocalDateTime invalidTime,

        Boolean isUsed,

        LocalDateTime usedDate,

        CouponResponseDto couponResponseDto

) {
}
