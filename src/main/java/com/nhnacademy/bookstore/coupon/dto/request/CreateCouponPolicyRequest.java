package com.nhnacademy.bookstore.coupon.dto.request;

import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

public record CreateCouponPolicyRequest(

        @NotBlank(message = "정책 이름이 존재하지 않습니다.")
        String name,

        @Enumerated(EnumType.STRING)
        @NotBlank(message = "할인 타입이 존재하지 않습니다.")
        DiscountType discountType,

        @NotBlank(message = "할인 값이 존재하지 않습니다.")
        Integer discountValue,

        @NotBlank(message = "사용 기한이 존재하지 않습니다.")
        Integer usageLimit,

        @NotBlank(message = "기간이 존재하지 않습니다.")
        Integer duration,

        @NotBlank(message = "정책 상세가 존재하지 않습니다.")
        String detail,

        @NotBlank(message = "최대 할인 금액이 존재하지 않습니다.")
        Integer maxDiscount,

        Boolean isActive
) {
}
