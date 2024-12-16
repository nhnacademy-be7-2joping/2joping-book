package com.nhnacademy.bookstore.shipment.dto.response;

/**
 * 배송 업체 response dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
public record CarrierResponseDto(
        Long carrierId,
        String name,
        String contactNumber,
        String contactEmail,
        String websiteUrl
) {
}

