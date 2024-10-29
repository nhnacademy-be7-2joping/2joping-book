package com.nhnacademy.bookstore.shipment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 배송 업체 request dto
 *
 * @author : 양준하
 * @date : 2024-10-29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarrierRequestDto {
    @NotBlank(message = "배송 업체 이름 입력: ")
    private String name;
    private String contactNumber;
    private String contactEmail;
    private String websiteUrl;
}
