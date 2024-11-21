package com.nhnacademy.bookstore.coupon.dto.response;

import java.time.LocalDate;


/**
 * CouponResponseDto
 * 이 클래스는 쿠폰에 대한 응답 데이터를 담고 있는 DTO입니다.
 * 쿠폰 ID, 이름, 생성일, 만료일, 수량, 그리고 쿠폰 정책에 대한 정보를 포함합니다.
 * 이 DTO는 쿠폰 정보를 클라이언트에 전달하기 위해 사용됩니다.
 *
 * @author Luha
 * @since 1.0
 */
public record CouponResponseDto(

        Long couponId,
        String name,
        LocalDate createdAt,
        LocalDate expiredAt,
        Integer quantity,
        CouponPolicyResponseDto couponPolicyResponseDto
) {
}
