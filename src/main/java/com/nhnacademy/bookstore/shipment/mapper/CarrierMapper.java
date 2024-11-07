package com.nhnacademy.bookstore.shipment.mapper;

import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Carrier;

public interface CarrierMapper {
    // Carrier Entity -> CarrierResponseDto 변환
    CarrierResponseDto toCarrierResponseDto(Carrier carrier);
}
