package com.nhnacademy.bookstore.shipment.repository;

import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShippingFeeResponseDto;

import java.util.List;

public interface ShipmentPolicyRepositoryCustom {
    List<ShipmentPolicyResponseDto> findActiveShipmentPolicies();
    List<ShippingFeeResponseDto> findActiveShippingFee(Boolean isMember);
}
