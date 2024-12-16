package com.nhnacademy.bookstore.shipment.dto.response;

public record ShippingFeeResponseDto(
        Long shipmentPolicyId,
        Integer minOrderAmount,
        Integer shippingFee
) {
}