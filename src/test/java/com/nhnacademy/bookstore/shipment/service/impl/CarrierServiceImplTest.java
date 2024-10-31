package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.shipment.dto.request.CarrierRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Carrier;
import com.nhnacademy.bookstore.shipment.repository.CarrierRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrierServiceImplTest {

    @Mock
    private CarrierRepository carrierRepository;

    @InjectMocks
    private CarrierServiceImpl carrierService;

    @Test
    @DisplayName("배송업체 생성 테스트")
    void createCarrier() {
        // given
        CarrierRequestDto requestDto = new CarrierRequestDto("핑핑배송", "010-1234-5678", "test@example.com", "https://example.com");
        Carrier savedCarrier = new Carrier(1L, "핑핑배송", "010-1234-5678", "test@example.com", "https://example.com");
        when(carrierRepository.save(any(Carrier.class))).thenReturn(savedCarrier);

        // when
        CarrierResponseDto responseDto = carrierService.createCarrier(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getCarrierId());
        assertEquals("핑핑배송", responseDto.getName());
    }

    @Test
    @DisplayName("특정 배송업체 조회 테스트")
    void getCarrier() {
        // given
        Carrier carrier = new Carrier(1L, "핑핑배송", "010-1234-5678", "test@example.com", "https://example.com");
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));

        // when
        CarrierResponseDto responseDto = carrierService.getCarrier(1L);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getCarrierId());
        assertEquals("핑핑배송", responseDto.getName());
    }

    @Test
    @DisplayName("모든 배송업체 조회 테스트")
    void getAllCarriers() {
        // given
        Carrier carrier1 = new Carrier(1L, "핑핑배송", "010-1234-5678", "test1@example.com", "https://example1.com");
        Carrier carrier2 = new Carrier(2L, "대한배송", "010-5678-1234", "test2@example.com", "https://example2.com");
        when(carrierRepository.findAll()).thenReturn(List.of(carrier1, carrier2));

        // when
        List<CarrierResponseDto> carriers = carrierService.getAllCarriers();

        // then
        assertNotNull(carriers);
        assertEquals(2, carriers.size());
        assertEquals("핑핑배송", carriers.get(0).getName());
        assertEquals("대한배송", carriers.get(1).getName());
    }

    @Test
    @DisplayName("배송업체 수정 테스트")
    void updateCarrier() {
        // given
        CarrierRequestDto requestDto = new CarrierRequestDto("이조핑배송", "010-5678-1234", "update@example.com", "https://updated.com");
        Carrier existingCarrier = new Carrier(1L, "핑핑배송", "010-1234-5678", "test@example.com", "https://example.com");
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(existingCarrier));
        when(carrierRepository.save(any(Carrier.class))).thenReturn(new Carrier(1L, "이조핑배송", "010-5678-1234", "update@example.com", "https://updated.com"));

        // when
        CarrierResponseDto responseDto = carrierService.updateCarrier(1L, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getCarrierId());
        assertEquals("이조핑배송", responseDto.getName());
    }

    @Test
    @DisplayName("배송업체 삭제 테스트")
    void deleteCarrier() {
        // given
        Carrier carrier = new Carrier(1L, "핑핑배송", "010-1234-5678", "test@example.com", "https://example.com");
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));
        doNothing().when(carrierRepository).delete(carrier);

        // when
        carrierService.deleteCarrier(1L);

        // then
        verify(carrierRepository, times(1)).delete(carrier);
    }
}
