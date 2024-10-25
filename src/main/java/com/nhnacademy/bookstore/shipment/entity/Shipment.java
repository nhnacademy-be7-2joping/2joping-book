package com.nhnacademy.bookstore.shipment.entity;
/**
 * 배송 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import com.nhnacademy.bookstore.orderset.order.entity.Order;
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

    @ManyToOne
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;

    @ManyToOne
    @JoinColumn(name = "shipment_policy_id", nullable = false)
    private ShipmentPolicy shipmentPolicy;


    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}

