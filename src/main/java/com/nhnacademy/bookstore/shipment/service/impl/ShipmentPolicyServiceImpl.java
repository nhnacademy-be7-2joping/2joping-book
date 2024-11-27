package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.common.error.exception.shipment.ShipmentPolicyNotFoundException;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentPolicyRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShippingFeeResponseDto;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.shipment.mapper.ShipmentPolicyMapper;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepository;
import com.nhnacademy.bookstore.shipment.service.ShipmentPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ShipmentPolicyService 구현체 클래스.
 * 배송 정책의 생성, 조회, 수정, 활성화 및 비활성화 기능을 제공합니다.
 *
 * @author 양준하
 */
@Service
@RequiredArgsConstructor
public class ShipmentPolicyServiceImpl implements ShipmentPolicyService {

    private final ShipmentPolicyRepository shipmentPolicyRepository;
    private final ShipmentPolicyMapper shipmentPolicyMapper;

    /**
     * 새로운 배송 정책을 생성하는 메서드입니다.
     *
     * @param requestDto 생성할 배송 정책 정보를 담은 DTO
     * @return 생성된 배송 정책의 정보를 포함한 ShipmentPolicyResponseDto
     */
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
        return shipmentPolicyMapper.toShipmentPolicyResponseDto(savedPolicy);
    }

    /**
     * 모든 배송 정책을 조회하는 메서드입니다.
     *
     * @return 배송 정책 목록을 포함한 ShipmentPolicyResponseDto 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ShipmentPolicyResponseDto> getAllShipmentPolicies(Pageable pageable) {
        return shipmentPolicyRepository.findAll(pageable)
                .map(shipmentPolicyMapper::toShipmentPolicyResponseDto);
    }

    /**
     * 모든 활성화된 배송 정책을 조회하는 메서드입니다.
     *
     * @return 활성화된 배송 정책 목록을 포함한 ShipmentPolicyResponseDto 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShipmentPolicyResponseDto> getAllIsActiveShipmentPolicies() {
        return shipmentPolicyRepository.findActiveShipmentPolicies();
    }

    /**
     * 특정 ID의 배송 정책을 조회하는 메서드입니다.
     *
     * @param shipmentPolicyId 조회할 배송 정책의 ID
     * @return 조회된 배송 정책의 정보를 포함한 ShipmentPolicyResponseDto
     * @throws ShipmentPolicyNotFoundException 배송 정책을 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional(readOnly = true)
    public ShipmentPolicyResponseDto getShipmentPolicy(Long shipmentPolicyId) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        return shipmentPolicyMapper.toShipmentPolicyResponseDto(policy);
    }

    /**
     * 특정 ID의 배송 정책을 수정하는 메서드입니다.
     *
     * @param shipmentPolicyId 수정할 배송 정책의 ID
     * @param requestDto 수정할 배송 정책 정보를 담은 DTO
     * @return 수정된 배송 정책의 정보를 포함한 ShipmentPolicyResponseDto
     * @throws ShipmentPolicyNotFoundException 배송 정책을 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public ShipmentPolicyResponseDto updateShipmentPolicy(Long shipmentPolicyId, ShipmentPolicyRequestDto requestDto) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        policy.toEntity(requestDto);
        ShipmentPolicy updatedPolicy = shipmentPolicyRepository.save(policy);
        return shipmentPolicyMapper.toShipmentPolicyResponseDto(updatedPolicy);
    }

    /**
     * 특정 배송 정책을 비활성화하는 메서드입니다.
     *
     * @param shipmentPolicyId 비활성화할 배송 정책의 ID
     * @throws ShipmentPolicyNotFoundException 배송 정책을 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public void deactivateShipmentPolicy(Long shipmentPolicyId) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        policy.deactivate();
        shipmentPolicyRepository.save(policy);
    }

    /**
     * 특정 배송 정책을 활성화하는 메서드입니다.
     *
     * @param shipmentPolicyId 활성화할 배송 정책의 ID
     * @throws ShipmentPolicyNotFoundException 배송 정책을 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public void activateShipmentPolicy(Long shipmentPolicyId) {
        ShipmentPolicy policy = shipmentPolicyRepository.findById(shipmentPolicyId)
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        policy.activate();
        shipmentPolicyRepository.save(policy);
    }

    /**
     * 모든 활성화된 배송 정책을 조회하는 메서드입니다.
     *
     * @return 활성화된 배송 정책 목록을 포함한 ShipmentPolicyResponseDto 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShippingFeeResponseDto> getShippingFee(Boolean isLogin) {
        return shipmentPolicyRepository.findActiveShippingFee(isLogin);
    }
}
