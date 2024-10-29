package com.nhnacademy.bookstore.shipment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 배송 업체 response dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarrierResponseDto {
    private Long carrierId;
    private String name;
    private String contactNumber;
    private String contactEmail;
    private String websiteUrl;
}
