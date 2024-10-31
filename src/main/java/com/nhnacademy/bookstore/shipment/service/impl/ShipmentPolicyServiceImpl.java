package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.common.error.exception.shipment.ShipmentPolicyNotFoundException;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentPolicyRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
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

    @Override
    @Transactional
    public ShipmentPolicyResponseDto createShipmentPolicy(ShipmentPolicyRequestDto requestDto) {
        ShipmentPolicy shipmentPolicy = new ShipmentPolicy(
                null,
                requestDto.getName(),
                requestDto.getMinOrderAmount(),
                requestDto.getIsMemberOnly(),
                LocalDateTime.now(),
                null,
                requestDto.getShippingFee(),
                true
        );

        ShipmentPolicy savedPolicy = shipmentPolicyRepository.save(shipmentPolicy);
        return mapToResponseDto(savedPolicy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentPolicyResponseDto> getAllShipmentPolicies() {
        return shipmentPolicyRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentPolicyResponseDto getShipmentPolicy(Long shipmentPolicyId) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        return mapToResponseDto(policy);
    }

    @Override
    @Transactional
    public ShipmentPolicyResponseDto updateShipmentPolicy(Long shipmentPolicyId, ShipmentPolicyRequestDto requestDto) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        policy.setName(requestDto.getName());
        policy.setMinOrderAmount(requestDto.getMinOrderAmount());
        policy.setIsMemberOnly(requestDto.getIsMemberOnly());
        policy.setShippingFee(requestDto.getShippingFee());
        policy.setUpdatedAt(LocalDateTime.now());

        ShipmentPolicy updatedPolicy = shipmentPolicyRepository.save(policy);
        return mapToResponseDto(updatedPolicy);
    }

    @Override
    @Transactional
    public void deactivateShipmentPolicy(Long shipmentPolicyId) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        policy.setIsActive(false);
        policy.setUpdatedAt(LocalDateTime.now());
        shipmentPolicyRepository.save(policy);
    }

    private ShipmentPolicyResponseDto mapToResponseDto(ShipmentPolicy shipmentPolicy) {
        return new ShipmentPolicyResponseDto(
                shipmentPolicy.getShipmentPolicyId(),
                shipmentPolicy.getName(),
                shipmentPolicy.getMinOrderAmount(),
                shipmentPolicy.getIsMemberOnly(),
                shipmentPolicy.getShippingFee(),
                shipmentPolicy.getIsActive(),
                shipmentPolicy.getCreatedAt(),
                shipmentPolicy.getUpdatedAt()
        );
    }
}
