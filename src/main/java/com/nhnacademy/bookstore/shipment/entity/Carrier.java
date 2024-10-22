package com.nhnacademy.bookstore.shipment.entity;

/**
 * 배송업체 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrier")
public class Carrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrier_id", nullable = false)
    private Long carrierId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(name = "tracking_url", nullable = false, length = 255)
    private String trackingUrl;
}
