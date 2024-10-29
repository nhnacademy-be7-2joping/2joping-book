package com.nhnacademy.bookstore.shipment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 배송 response dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponseDto {
    private Long shipmentId;
    private Long carrierId;
    private Long shipmentPolicyId;
    private Long orderId;
    private String requirement;
    private LocalDateTime shippingDate;
    private LocalDateTime deliveryDate;
    private String trackingNumber;
}
