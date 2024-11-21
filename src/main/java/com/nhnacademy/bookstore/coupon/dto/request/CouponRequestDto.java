package com.nhnacademy.bookstore.coupon.dto.request;


import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * CouponRequestDto
 * 이 클래스는 쿠폰 생성 요청을 위한 데이터를 담고 있는 DTO입니다.
 * 쿠폰 정책 ID, 쿠폰 이름, 만료일, 수량 정보를 포함하며, 수량은 선택적으로 설정할 수 있습니다.
 * 기본 생성자는 수량을 생략할 수 있도록 오버로딩되어 있습니다.

 * @author Luha
 * @since 1.0
 */
public record CouponRequestDto(

        @NotNull(message = "쿠폰 정책 ID는 필수입니다.")
        Long couponPolicyId,

        @NotBlank(message = "쿠폰 이름은 필수입니다.")
        @Size(max = 30, message = "쿠폰 이름은 최대 30자까지 입력할 수 있습니다.")
        String name,

        @NotNull(message = "쿠폰 만료일은 필수입니다.")
        @Future(message = "쿠폰 만료일은 오늘 이후여야 합니다.")
        LocalDate expiredAt,

        @Positive(message = "쿠폰 수량은 양수여야 합니다.")
        Integer quantity
) {
    public CouponRequestDto(Long couponPolicyId,
                            String name,
                            LocalDate expiredAt) {
        this(couponPolicyId, name, expiredAt, null);
    }
}
