package com.nhnacademy.bookstore.shipment.entity;
/**
 * 배송정책 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "shipment_policy")
@Getter
@Setter
@NoArgsConstructor
public class ShipmentPolicy {

    @Id
    @Column(name = "shipment_policy_id", nullable = false)
    private Long shipmentPolicyId;

    @Column(name = "policy_name", length = 255, nullable = false, unique = true)
    private String policyName;

    @Column(name = "min_order_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal minOrderAmount;

    @Column(name = "is_member_only", nullable = false)
    private boolean isMemberOnly;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;
}