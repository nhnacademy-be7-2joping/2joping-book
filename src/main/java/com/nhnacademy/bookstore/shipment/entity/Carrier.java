package com.nhnacademy.bookstore.shipment.entity;

/**
 * 배송업체 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */

import com.nhnacademy.bookstore.shipment.dto.request.CarrierRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carrier")
public class Carrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    public void toEntity(CarrierRequestDto requestDto) {
        this.name = requestDto.name();
        this.contactNumber = requestDto.contactNumber();
        this.contactEmail = requestDto.contactEmail();
        this.websiteUrl = requestDto.websiteUrl();
    }
}
