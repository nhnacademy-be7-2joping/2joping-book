package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.shipment.dto.request.ShipmentPolicyRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShippingFeeResponseDto;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.shipment.mapper.ShipmentPolicyMapper;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentPolicyServiceImplTest {

    @Mock
    private ShipmentPolicyRepository shipmentPolicyRepository;

    @Mock
    private ShipmentPolicyMapper shipmentPolicyMapper;

    @InjectMocks
    private ShipmentPolicyServiceImpl shipmentPolicyService;

    @Test
    @DisplayName("배송 정책 생성 테스트")
    void createShipmentPolicy() {
        // given
        ShipmentPolicyRequestDto requestDto = new ShipmentPolicyRequestDto("정책 이름", 10000, true, 5000);
        ShipmentPolicy savedPolicy = new ShipmentPolicy(1L, "정책 이름", 10000, true, null, null, 5000, true);
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "정책 이름", 10000, true, null, null, 5000, true);

        when(shipmentPolicyRepository.save(any(ShipmentPolicy.class))).thenReturn(savedPolicy);
        when(shipmentPolicyMapper.toShipmentPolicyResponseDto(savedPolicy)).thenReturn(responseDto); // Mapper 사용

        // when
        ShipmentPolicyResponseDto result = shipmentPolicyService.createShipmentPolicy(requestDto);

        // then
        assertNotNull(result);
        assertEquals(1L, result.shipmentPolicyId());
        assertEquals("정책 이름", result.name());
    }

    @Test
    @DisplayName("특정 배송 정책 조회 테스트")
    void getShipmentPolicy() {
        // given
        ShipmentPolicy policy = new ShipmentPolicy(1L, "정책 이름", 10000, true, null, null, 5000, true);
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "정책 이름", 10000, true, null, null, 5000, true);

        when(shipmentPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(shipmentPolicyMapper.toShipmentPolicyResponseDto(policy)).thenReturn(responseDto);

        // when
        ShipmentPolicyResponseDto result = shipmentPolicyService.getShipmentPolicy(1L);

        // then
        assertNotNull(result);
        assertEquals(1L, result.shipmentPolicyId());
        assertEquals("정책 이름", result.name());
    }

    @Test
    @DisplayName("배송 정책 수정 테스트")
    void updateShipmentPolicy() {
        // given
        ShipmentPolicyRequestDto requestDto = new ShipmentPolicyRequestDto("업데이트된 정책", 5000, false, 3000);
        ShipmentPolicy existingPolicy = new ShipmentPolicy(1L, "정책 이름", 10000, true, null, null, 5000, true);
        ShipmentPolicy updatedPolicy = new ShipmentPolicy(1L, "업데이트된 정책", 5000, false, null, null, 3000, true);
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "업데이트된 정책", 5000, false, null, null, 3000, true);

        when(shipmentPolicyRepository.findById(1L)).thenReturn(Optional.of(existingPolicy));
        when(shipmentPolicyRepository.save(any(ShipmentPolicy.class))).thenReturn(updatedPolicy);
        when(shipmentPolicyMapper.toShipmentPolicyResponseDto(updatedPolicy)).thenReturn(responseDto);

        // when
        ShipmentPolicyResponseDto result = shipmentPolicyService.updateShipmentPolicy(1L, requestDto);

        // then
        assertEquals("업데이트된 정책", result.name());
        assertEquals(5000, result.minOrderAmount());
        assertEquals(3000, result.shippingFee());
    }

    @Test
    @DisplayName("배송 정책 비활성화 테스트")
    void deactivateShipmentPolicy() {
        // given
        ShipmentPolicy policy = new ShipmentPolicy(1L, "정책 이름", 10000, true, null, null, 5000, true);
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "정책 이름", 10000, true, null, null, 5000, false);

        when(shipmentPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

        // when
        shipmentPolicyService.deactivateShipmentPolicy(1L);

        // then
        assertFalse(policy.getIsActive());
    }

    @Test
    @DisplayName("배송 정책 활성화 테스트")
    void activateShipmentPolicy() {
        // given
        ShipmentPolicy policy = new ShipmentPolicy(1L, "정책 이름", 10000, true, null, null, 5000, false);

        when(shipmentPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

        // when
        shipmentPolicyService.activateShipmentPolicy(1L);

        // then
        assertTrue(policy.getIsActive());
    }


    @Test
    @DisplayName("모든 배송 정책 페이징 조회 테스트")
    void getAllShipmentPolicies_Service() {
        // given
        ShipmentPolicy shipmentPolicy1 = new ShipmentPolicy(1L, "정책1", 10000, true, null, null, 5000, true);
        ShipmentPolicy shipmentPolicy2 = new ShipmentPolicy(2L, "정책2", 5000, false, null, null, 2500, true);

        ShipmentPolicyResponseDto responseDto1 = new ShipmentPolicyResponseDto(1L, "정책1", 10000, true, null, null, 5000, true);
        ShipmentPolicyResponseDto responseDto2 = new ShipmentPolicyResponseDto(2L, "정책2", 5000, false, null, null, 2500, true);

        Page<ShipmentPolicy> shipmentPolicyPage = new PageImpl<>(List.of(shipmentPolicy1, shipmentPolicy2));

        when(shipmentPolicyRepository.findAll(any(Pageable.class))).thenReturn(shipmentPolicyPage);
        when(shipmentPolicyMapper.toShipmentPolicyResponseDto(shipmentPolicy1)).thenReturn(responseDto1);
        when(shipmentPolicyMapper.toShipmentPolicyResponseDto(shipmentPolicy2)).thenReturn(responseDto2);

        // when
        Page<ShipmentPolicyResponseDto> result = shipmentPolicyService.getAllShipmentPolicies(PageRequest.of(0, 10));

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("정책1", result.getContent().get(0).name());
        assertEquals("정책2", result.getContent().get(1).name());
    }

    @Test
    @DisplayName("모든 활성화된 배송 정책 조회 테스트")
    void getAllIsActiveShipmentPolicies() {
        // given
        ShipmentPolicyResponseDto responseDto1 = new ShipmentPolicyResponseDto(1L, "정책 1", 10000, true, null, null, 5000, true);
        ShipmentPolicyResponseDto responseDto2 = new ShipmentPolicyResponseDto(2L, "정책 2", 5000, false, null, null, 3000, true);

        when(shipmentPolicyRepository.findActiveShipmentPolicies()).thenReturn(List.of(responseDto1, responseDto2));

        // when
        List<ShipmentPolicyResponseDto> responseList = shipmentPolicyService.getAllIsActiveShipmentPolicies();

        // then
        assertNotNull(responseList);
        assertEquals(2, responseList.size());
        assertEquals("정책 1", responseList.get(0).name());
        assertEquals("정책 2", responseList.get(1).name());
    }

    @Test
    @DisplayName("회원 여부에 따른 활성화된 배송비 조회 테스트")
    void getShippingFee() {
        // given
        Boolean isMember = true; // 예: 회원 여부가 true
        ShippingFeeResponseDto responseDto1 = new ShippingFeeResponseDto(1L, 10000, 5000);
        ShippingFeeResponseDto responseDto2 = new ShippingFeeResponseDto(2L, 20000, 3000);

        when(shipmentPolicyRepository.findActiveShippingFee(isMember)).thenReturn(List.of(responseDto1, responseDto2));

        // when
        List<ShippingFeeResponseDto> result = shipmentPolicyService.getShippingFee(isMember);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10000, result.get(0).minOrderAmount());
        assertEquals(5000, result.get(0).shippingFee());
        assertEquals(20000, result.get(1).minOrderAmount());
        assertEquals(3000, result.get(1).shippingFee());
    }


}
