package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.common.error.exception.shipment.CarrierNotFoundException;
import com.nhnacademy.bookstore.shipment.dto.request.CarrierRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Carrier;
import com.nhnacademy.bookstore.shipment.mapper.CarrierMapper;
import com.nhnacademy.bookstore.shipment.repository.CarrierRepository;
import com.nhnacademy.bookstore.shipment.service.CarrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CarrierService 구현체 클래스.
 * 배송 업체의 생성, 조회, 수정 및 삭제 기능을 제공합니다.
 *
 * @author 양준하
 */
@Service
@RequiredArgsConstructor
public class CarrierServiceImpl implements CarrierService {

    private final CarrierRepository carrierRepository;
    private final CarrierMapper carrierMapper;

    /**
     * 새로운 배송 업체를 생성하는 메서드입니다.
     *
     * @param requestDto 생성할 배송 업체 정보를 담은 DTO
     * @return 생성된 배송 업체의 정보를 포함한 CarrierResponseDto
     */
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

    /**
     * 특정 배송 업체를 ID로 조회하는 메서드입니다.
     *
     * @param carrierId 조회할 배송 업체의 ID
     * @return 조회된 배송 업체의 정보를 포함한 CarrierResponseDto
     * @throws CarrierNotFoundException 배송 업체를 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional(readOnly = true)
    public CarrierResponseDto getCarrier(Long carrierId) {
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(CarrierNotFoundException::new);
        return carrierMapper.toCarrierResponseDto(carrier);
    }

    /**
     * 모든 배송 업체를 조회하는 메서드입니다.
     *
     * @return 모든 배송 업체 목록을 포함한 CarrierResponseDto 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<CarrierResponseDto> getAllCarriers() {
        List<Carrier> carriers = carrierRepository.findAll();
        return carriers.stream()
                .map(carrierMapper::toCarrierResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 배송 업체를 수정하는 메서드입니다.
     *
     * @param carrierId  수정할 배송 업체의 ID
     * @param requestDto 수정할 배송 업체 정보를 담은 DTO
     * @return 수정된 배송 업체의 정보를 포함한 CarrierResponseDto
     * @throws CarrierNotFoundException 배송 업체를 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public CarrierResponseDto updateCarrier(Long carrierId, CarrierRequestDto requestDto) {
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(CarrierNotFoundException::new);
        carrier.toEntity(requestDto);
        Carrier updatedCarrier = carrierRepository.save(carrier);
        return carrierMapper.toCarrierResponseDto(updatedCarrier);
    }

    /**
     * 특정 배송 업체를 삭제하는 메서드입니다.
     *
     * @param carrierId 삭제할 배송 업체의 ID
     * @throws CarrierNotFoundException 배송 업체를 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public void deleteCarrier(Long carrierId) {
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(CarrierNotFoundException::new);
        carrierRepository.delete(carrier);
    }
}
