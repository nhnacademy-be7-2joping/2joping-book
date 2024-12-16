package com.nhnacademy.bookstore.shipment.repository;

import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ShipmentRepositoryCustom {
    List<ShipmentResponseDto> findAllShipmentDtos();

    List<ShipmentResponseDto> findCompletedShipmentDtos(LocalDateTime now);

    List<ShipmentResponseDto> findPendingShipmentDtos(LocalDateTime now);
}