package com.nhnacademy.bookstore.shipment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 배송 request dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRequestDto {
    @NotNull
    private Long carrierId;
    @NotNull
    private Long shipmentPolicyId;
    @NotNull
    private Long orderId;
    private String requirement;
    private LocalDateTime shippingDate;
    private LocalDateTime deliveryDate;
    @NotNull
    private String trackingNumber;
}
