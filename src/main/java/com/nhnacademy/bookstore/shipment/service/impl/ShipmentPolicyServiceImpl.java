package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.common.error.exception.shipment.ShipmentPolicyNotFoundException;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentPolicyRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.shipment.mapper.ShipmentMapper;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepository;
import com.nhnacademy.bookstore.shipment.service.ShipmentPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentPolicyServiceImpl implements ShipmentPolicyService {

    private final ShipmentPolicyRepository shipmentPolicyRepository;
    private final ShipmentMapper shipmentMapper;

    @Override
    @Transactional
    public ShipmentPolicyResponseDto createShipmentPolicy(ShipmentPolicyRequestDto requestDto) {
        ShipmentPolicy shipmentPolicy = new ShipmentPolicy(
                null,
                requestDto.name(),
                requestDto.minOrderAmount(),
                requestDto.isMemberOnly(),
                LocalDateTime.now(),
                null,
                requestDto.shippingFee(),
                true
        );

        ShipmentPolicy savedPolicy = shipmentPolicyRepository.save(shipmentPolicy);
        return shipmentMapper.toShipmentPolicyResponseDto(savedPolicy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentPolicyResponseDto> getAllShipmentPolicies() {
        return shipmentPolicyRepository.findByIsActiveTrue()
                .stream()
                .map(shipmentMapper::toShipmentPolicyResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentPolicyResponseDto getShipmentPolicy(Long shipmentPolicyId) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        return shipmentMapper.toShipmentPolicyResponseDto(policy);
    }

    @Override
    @Transactional
    public ShipmentPolicyResponseDto updateShipmentPolicy(Long shipmentPolicyId, ShipmentPolicyRequestDto requestDto) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        policy.toEntity(requestDto);
        ShipmentPolicy updatedPolicy = shipmentPolicyRepository.save(policy);
        return shipmentMapper.toShipmentPolicyResponseDto(updatedPolicy);
    }

    @Override
    @Transactional
    public void deactivateShipmentPolicy(Long shipmentPolicyId) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        policy.deactivate();
        shipmentPolicyRepository.save(policy);
    }

    @Override
    @Transactional
    public void activateShipmentPolicy(Long shipmentPolicyId) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        policy.activate();
        shipmentPolicyRepository.save(policy);
    }
}
