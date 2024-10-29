package com.nhnacademy.bookstore.shipment.service;

import com.nhnacademy.bookstore.shipment.dto.request.ShipmentPolicyRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;

import java.util.List;

public interface ShipmentPolicyService {
    ShipmentPolicyResponseDto createShipmentPolicy(ShipmentPolicyRequestDto requestDto);
    ShipmentPolicyResponseDto getShipmentPolicy(Long shipmentPolicyId);
    List<ShipmentPolicyResponseDto> getAllShipmentPolicies();
    ShipmentPolicyResponseDto updateShipmentPolicy(Long shipmentPolicyId, ShipmentPolicyRequestDto requestDto);
    void deactivateShipmentPolicy(Long shipmentPolicyId);
}
