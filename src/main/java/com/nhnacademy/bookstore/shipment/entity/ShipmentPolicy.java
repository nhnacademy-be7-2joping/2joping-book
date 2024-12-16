package com.nhnacademy.bookstore.shipment.entity;
/**
 * 배송정책 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */

import com.nhnacademy.bookstore.shipment.dto.request.ShipmentPolicyRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shipment_policy")
public class ShipmentPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_policy_id")
    private Long shipmentPolicyId;

    @Column(name = "name", length = 255, unique = true)
    private String name;

    @Column(name = "min_order_amount")
    private Integer minOrderAmount;

    @Column(name = "is_member_only")
    private Boolean isMemberOnly;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "shipping_fee")
    private Integer shippingFee;

    @Column(name = "is_active")
    private Boolean isActive;

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void toEntity(ShipmentPolicyRequestDto requestDto) {
        this.name = requestDto.name();
        this.minOrderAmount = requestDto.minOrderAmount();
        this.isMemberOnly = requestDto.isMemberOnly();
        this.shippingFee = requestDto.shippingFee();
        this.updatedAt = LocalDateTime.now();
    }
}
