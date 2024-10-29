package com.nhnacademy.bookstore.shipment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * 배송 정책 request dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentPolicyRequestDto {
    @NotBlank(message = "배송 정책 이름 입력: ")
    private String name;
    @NotNull(message = "최소 주문 금액 입력: ")
    private Integer minOrderAmount;
    @NotNull
    private Boolean isMemberOnly;
    @NotNull(message = "배송비 입력: ")
    private Integer shippingFee;
}


