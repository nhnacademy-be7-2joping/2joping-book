package com.nhnacademy.bookstore.shipment.entity;
/**
 * 배송 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipment")
@Getter
@Setter
@NoArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId;

    @Column(name = "requirement", length = 32)
    private String requirement;

    @Column(name = "desired_delivery_date")
    private LocalDateTime desiredDeliveryDate;

    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Column(name = "delivery_date", nullable = false)
    private LocalDateTime deliveryDate;

    @Column(name = "tracking_number", length = 255, nullable = false)
    private String trackingNumber;

    @Column(name = "carrier_id", nullable = false)
    private Long carrierId;

    @Column(name = "shipment_policy_id", nullable = false)
    private Long shipmentPolicyId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;
}

