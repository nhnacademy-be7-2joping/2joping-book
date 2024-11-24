package com.nhnacademy.bookstore.coupon.dto.request;

import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCouponPolicyRequest(

        @NotBlank(message = "정책 이름이 존재하지 않습니다.")
        String name,

        @Enumerated(EnumType.STRING)
        @NotNull(message = "할인 타입이 존재하지 않습니다.")
        DiscountType discountType,

        @NotNull(message = "할인 값이 존재하지 않습니다.")
        @Positive(message = "할인 값은 0보다 커야 합니다.")
        Integer discountValue,

        @NotNull(message = "사용 기한이 존재하지 않습니다.")
        @Positive(message = "사용 기한은 0보다 커야 합니다.")
        Integer usageLimit,

        @NotNull(message = "기간이 존재하지 않습니다.")
        @Positive(message = "기간은 0보다 커야 합니다.")
        Integer duration,

        @NotBlank(message = "정책 상세가 존재하지 않습니다.")
        String detail,

        @NotNull(message = "최대 할인 금액이 존재하지 않습니다.")
        @Positive(message = "최대 할인 금액은 0보다 커야 합니다.")
        Integer maxDiscount,

        Boolean isActive
) {
}
