package com.nhnacademy.bookstore.shipment.service;

import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;

import java.util.List;

public interface ShipmentService {
    ShipmentResponseDto createShipment(ShipmentRequestDto requestDto);

    ShipmentResponseDto getShipment(Long shipmentId);

    List<ShipmentResponseDto> getAllShipments();

    List<ShipmentResponseDto> getCompletedShipments();

    List<ShipmentResponseDto> getPendingShipments();

    ShipmentResponseDto updateShipment(Long shipmentId, ShipmentRequestDto requestDto);

    void deleteShipment(Long shipmentId);
}
