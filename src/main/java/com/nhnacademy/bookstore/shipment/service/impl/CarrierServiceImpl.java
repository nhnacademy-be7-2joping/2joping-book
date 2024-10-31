package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.common.error.exception.shipment.CarrierNotFoundException;
import com.nhnacademy.bookstore.shipment.dto.request.CarrierRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Carrier;
import com.nhnacademy.bookstore.shipment.repository.CarrierRepository;
import com.nhnacademy.bookstore.shipment.service.CarrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarrierServiceImpl implements CarrierService {

    private final CarrierRepository carrierRepository;

    @Override
    @Transactional
    public CarrierResponseDto createCarrier(CarrierRequestDto requestDto) {
        Carrier carrier = new Carrier(
                null,
                requestDto.getName(),
                requestDto.getContactNumber(),
                requestDto.getContactEmail(),
                requestDto.getWebsiteUrl()
        );

        Carrier savedCarrier = carrierRepository.save(carrier);
        return mapToResponseDto(savedCarrier);
    }

    @Override
    @Transactional(readOnly = true)
    public CarrierResponseDto getCarrier(Long carrierId) {
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(CarrierNotFoundException::new);
        return mapToResponseDto(carrier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarrierResponseDto> getAllCarriers() {
        return carrierRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CarrierResponseDto updateCarrier(Long carrierId, CarrierRequestDto requestDto) {
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(CarrierNotFoundException::new);
        carrier.setName(requestDto.getName());
        carrier.setContactNumber(requestDto.getContactNumber());
        carrier.setContactEmail(requestDto.getContactEmail());
        carrier.setWebsiteUrl(requestDto.getWebsiteUrl());

        Carrier updatedCarrier = carrierRepository.save(carrier);
        return mapToResponseDto(updatedCarrier);
    }

    @Override
    @Transactional
    public void deleteCarrier(Long carrierId) {
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(CarrierNotFoundException::new);
        carrierRepository.delete(carrier);
    }

    private CarrierResponseDto mapToResponseDto(Carrier carrier) {
        return new CarrierResponseDto(
                carrier.getCarrierId(),
                carrier.getName(),
                carrier.getContactNumber(),
                carrier.getContactEmail(),
                carrier.getWebsiteUrl()
        );
    }
}
