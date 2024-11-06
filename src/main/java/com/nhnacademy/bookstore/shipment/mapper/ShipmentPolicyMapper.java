package com.nhnacademy.bookstore.shipment.mapper;

import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;

public interface ShipmentPolicyMapper {

    // ShipmentPolicy Entity -> ShipmentPolicyResponseDto 변환
    ShipmentPolicyResponseDto toShipmentPolicyResponseDto(ShipmentPolicy shipmentPolicy);
}
