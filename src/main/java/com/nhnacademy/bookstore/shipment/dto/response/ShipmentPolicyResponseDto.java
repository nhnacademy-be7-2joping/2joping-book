package com.nhnacademy.bookstore.shipment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 배송 정책 response dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentPolicyResponseDto {
    private Long shipmentPolicyId;
    private String name;
    private Integer minOrderAmount;
    private Boolean isMemberOnly;
    private Integer shippingFee;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
