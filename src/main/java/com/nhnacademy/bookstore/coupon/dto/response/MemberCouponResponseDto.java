package com.nhnacademy.bookstore.coupon.dto.response;

import java.time.LocalDateTime;

/**
 * MemberCouponResponseDto
 * 이 클래스는 회원이 보유한 쿠폰에 대한 응답 데이터를 담고 있는 DTO입니다.
 * 쿠폰 사용 ID, 쿠폰 ID, 쿠폰 수령 시간, 만료 시간, 사용 여부, 사용 일자 및 쿠폰에 대한 상세 정보를 포함합니다.
 * 이 DTO는 특정 회원이 소유한 쿠폰 정보를 클라이언트에 전달하기 위해 사용됩니다.
 *
 * @author Luha
 * @since 1.0
 */
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
