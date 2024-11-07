package com.nhnacademy.bookstore.shipment.mapper;

import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Carrier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarrierMapper {
    // Carrier Entity -> CarrierResponseDto 변환
    CarrierResponseDto toCarrierResponseDto(Carrier carrier);
}
