package com.nhnacademy.bookstore.shipment.mapper;

import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    // Shipment Entity -> ShipmentResponseDto 변환
    @Mapping(source = "carrier.carrierId", target = "carrierId")
    @Mapping(source = "shipmentPolicy.shipmentPolicyId", target = "shipmentPolicyId")
    @Mapping(source = "order.orderId", target = "orderId")
    ShipmentResponseDto toShipmentResponseDto(Shipment shipment);
}
