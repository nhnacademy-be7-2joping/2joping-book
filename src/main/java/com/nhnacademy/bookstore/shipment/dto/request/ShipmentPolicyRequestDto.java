package com.nhnacademy.bookstore.shipment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 배송 정책 request dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
public record ShipmentPolicyRequestDto(
        @NotBlank(message = "배송 정책 이름 입력: ") String name,
        @NotNull(message = "최소 주문 금액 입력: ") Integer minOrderAmount,
        @NotNull Boolean isMemberOnly,
        @NotNull(message = "배송비 입력: ") Integer shippingFee
) {
}
