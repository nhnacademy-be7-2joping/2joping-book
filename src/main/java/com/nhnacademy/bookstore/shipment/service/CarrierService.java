package com.nhnacademy.bookstore.shipment.service;

import com.nhnacademy.bookstore.shipment.dto.request.CarrierRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;

import java.util.List;

public interface CarrierService {
    CarrierResponseDto createCarrier(CarrierRequestDto requestDto);
    CarrierResponseDto getCarrier(Long carrierId);
    List<CarrierResponseDto> getAllCarriers();
    CarrierResponseDto updateCarrier(Long carrierId, CarrierRequestDto requestDto);
    void deleteCarrier(Long carrierId);
}
