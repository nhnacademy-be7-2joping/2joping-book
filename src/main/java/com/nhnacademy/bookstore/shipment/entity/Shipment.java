package com.nhnacademy.bookstore.shipment.entity;
/**
 * 배송 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */

import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long shipmentId;

    @ManyToOne
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;

    @ManyToOne
    @JoinColumn(name = "shipment_policy_id")
    private ShipmentPolicy shipmentPolicy;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "requirement", length = 32)
    private String requirement;

    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "tracking_number", length = 255)
    private String trackingNumber;

    public void toEntity(ShipmentRequestDto requestDto, Carrier carrier, ShipmentPolicy policy, Order order) {
        this.carrier = carrier;
        this.shipmentPolicy = policy;
        this.order = order;
        this.requirement = requestDto.requirement();
        this.shippingDate = requestDto.shippingDate();
        this.deliveryDate = requestDto.deliveryDate();
        this.trackingNumber = requestDto.trackingNumber();
    }
}
