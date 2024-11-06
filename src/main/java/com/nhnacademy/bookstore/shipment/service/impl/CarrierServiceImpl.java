package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.common.error.exception.shipment.CarrierNotFoundException;
import com.nhnacademy.bookstore.shipment.dto.request.CarrierRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;
import com.nhnacademy.bookstore.shipment.mapper.CarrierMapper;
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
    private final CarrierMapper carrierMapper;

    @Override
    @Transactional
    public CarrierResponseDto createCarrier(CarrierRequestDto requestDto) {
        Carrier carrier = new Carrier(
                null,
                requestDto.name(),
                requestDto.contactNumber(),
                requestDto.contactEmail(),
                requestDto.websiteUrl()
        );

        Carrier savedCarrier = carrierRepository.save(carrier);
        return carrierMapper.toCarrierResponseDto(savedCarrier);
    }

    @Override
    @Transactional(readOnly = true)
    public CarrierResponseDto getCarrier(Long carrierId) {
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(CarrierNotFoundException::new);
        return carrierMapper.toCarrierResponseDto(carrier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarrierResponseDto> getAllCarriers() {
        List<Carrier> carriers = carrierRepository.findAll();
        return carriers.stream()
                .map(carrierMapper::toCarrierResponseDto)
                .collect(Collectors.toList());
//        return carrierRepository.findAllCarriers();
    }

    @Override
    @Transactional
    public CarrierResponseDto updateCarrier(Long carrierId, CarrierRequestDto requestDto) {
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(CarrierNotFoundException::new);
        carrier.toEntity(requestDto);
        Carrier updatedCarrier = carrierRepository.save(carrier);
        return carrierMapper.toCarrierResponseDto(updatedCarrier);
    }

    @Override
    @Transactional
    public void deleteCarrier(Long carrierId) {
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(CarrierNotFoundException::new);
        carrierRepository.delete(carrier);
    }
}
