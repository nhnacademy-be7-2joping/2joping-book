package com.nhnacademy.bookstore.shipment.dto.response;

import java.time.LocalDateTime;

/**
 * 배송 response dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
public record ShipmentResponseDto(
        Long shipmentId,
        Long carrierId,
        Long shipmentPolicyId,
        Long orderId,
        String requirement,
        LocalDateTime shippingDate,
        LocalDateTime deliveryDate,
        String trackingNumber
) {
}