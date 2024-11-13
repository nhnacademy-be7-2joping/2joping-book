package com.nhnacademy.bookstore.shipment.repository.impl;

import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShippingFeeResponseDto;
import com.nhnacademy.bookstore.shipment.entity.QShipmentPolicy;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepositoryCustom;
import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ShipmentPolicyRepositoryImpl extends QuerydslRepositorySupport implements ShipmentPolicyRepositoryCustom {

    public ShipmentPolicyRepositoryImpl() {
        super(ShipmentPolicy.class);
    }

    @Override
    public List<ShipmentPolicyResponseDto> findActiveShipmentPolicies() {
        QShipmentPolicy shipmentPolicy = QShipmentPolicy.shipmentPolicy;

        return from(shipmentPolicy)
                .select(Projections.constructor(ShipmentPolicyResponseDto.class,
                        shipmentPolicy.shipmentPolicyId,
                        shipmentPolicy.name,
                        shipmentPolicy.minOrderAmount,
                        shipmentPolicy.isMemberOnly,
                        shipmentPolicy.createdAt,
                        shipmentPolicy.updatedAt,
                        shipmentPolicy.shippingFee,
                        shipmentPolicy.isActive))
                .where(shipmentPolicy.isActive.isTrue())
                .fetch();
    }

    @Override
    public List<ShippingFeeResponseDto> findActiveShippingFee(Boolean isLogin) {
        QShipmentPolicy shipmentPolicy = QShipmentPolicy.shipmentPolicy;

        return from(shipmentPolicy)
                .select(Projections.constructor(ShippingFeeResponseDto.class,
                        shipmentPolicy.shipmentPolicyId,
                        shipmentPolicy.minOrderAmount,
                        shipmentPolicy.shippingFee))
                .where(shipmentPolicy.isActive.isTrue()
                        .and(shipmentPolicy.isMemberOnly.eq(isLogin)))
                .fetch();
    }
}
