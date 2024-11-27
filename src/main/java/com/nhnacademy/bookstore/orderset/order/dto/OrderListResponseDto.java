package com.nhnacademy.bookstore.orderset.order.dto;

import java.time.LocalDate;

public record OrderListResponseDto(
        Long orderId,
        int orderStateId,
        String customerName,
        String couponName,
        String receiver,
        String postalCode,
        String roadAddress,
        String detailAddress,
        Integer pointUsage,
        Integer shippingFee,
        Integer couponSalePrice,
        Integer totalPrice,
        LocalDate desiredDeliveryDate // LocalDate 타입
) {
}
