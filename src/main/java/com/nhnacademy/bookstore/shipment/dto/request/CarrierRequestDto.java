package com.nhnacademy.bookstore.shipment.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 배송 업체 request dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
public record CarrierRequestDto(
        @NotBlank(message = "배송 업체 이름 입력: ") String name,
        String contactNumber,
        String contactEmail,
        String websiteUrl
) {
}
