package com.nhnacademy.bookstore.shipment.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * 배송 request dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
public record ShipmentRequestDto(
        Long carrierId,
        @NotNull Long shipmentPolicyId,
        @NotNull Long orderId,
        String requirement,
        LocalDateTime shippingDate,
        LocalDateTime deliveryDate,
        String trackingNumber
) {
}
