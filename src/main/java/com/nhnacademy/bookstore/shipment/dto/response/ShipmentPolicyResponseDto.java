package com.nhnacademy.bookstore.shipment.dto.response;

import java.time.LocalDateTime;

/**
 * 배송 정책 response dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
public record ShipmentPolicyResponseDto(
        Long shipmentPolicyId,
        String name,
        Integer minOrderAmount,
        Boolean isMemberOnly,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer shippingFee,
        Boolean isActive
) {
}
