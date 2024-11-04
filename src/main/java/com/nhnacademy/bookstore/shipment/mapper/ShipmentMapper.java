package com.nhnacademy.bookstore.shipment.mapper;

import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Carrier;
import com.nhnacademy.bookstore.shipment.entity.Shipment;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    ShipmentMapper INSTANCE = Mappers.getMapper(ShipmentMapper.class);

    // Carrier Entity -> CarrierResponseDto 변환
    @Mapping(source = "carrier.carrierId", target = "carrierId")
    @Mapping(source = "carrier.name", target = "name")
    @Mapping(source = "carrier.contactNumber", target = "contactNumber")
    @Mapping(source = "carrier.contactEmail", target = "contactEmail")
    @Mapping(source = "carrier.websiteUrl", target = "websiteUrl")
    CarrierResponseDto toCarrierResponseDto(Carrier carrier);

    // ShipmentPolicy Entity -> ShipmentPolicyResponseDto 변환
    @Mapping(source = "shipmentPolicy.shipmentPolicyId", target = "shipmentPolicyId")
    @Mapping(source = "shipmentPolicy.name", target = "name")
    @Mapping(source = "shipmentPolicy.minOrderAmount", target = "minOrderAmount")
    @Mapping(source = "shipmentPolicy.isMemberOnly", target = "isMemberOnly")
    @Mapping(source = "shipmentPolicy.shippingFee", target = "shippingFee")
    @Mapping(source = "shipmentPolicy.isActive", target = "isActive")
    @Mapping(source = "shipmentPolicy.createdAt", target = "createdAt")
    @Mapping(source = "shipmentPolicy.updatedAt", target = "updatedAt")
    ShipmentPolicyResponseDto toShipmentPolicyResponseDto(ShipmentPolicy shipmentPolicy);

    // Shipment Entity -> ShipmentResponseDto 변환
    @Mapping(source = "shipment.shipmentId", target = "shipmentId")
    @Mapping(source = "shipment.carrier.carrierId", target = "carrierId")
    @Mapping(source = "shipment.shipmentPolicy.shipmentPolicyId", target = "shipmentPolicyId")
    @Mapping(source = "shipment.order.orderId", target = "orderId")
    @Mapping(source = "shipment.requirement", target = "requirement")
    @Mapping(source = "shipment.shippingDate", target = "shippingDate")
    @Mapping(source = "shipment.deliveryDate", target = "deliveryDate")
    @Mapping(source = "shipment.trackingNumber", target = "trackingNumber")
    ShipmentResponseDto toShipmentResponseDto(Shipment shipment);
}
