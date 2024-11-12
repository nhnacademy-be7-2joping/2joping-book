package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Carrier;
import com.nhnacademy.bookstore.shipment.entity.Shipment;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.shipment.mapper.ShipmentMapper;
import com.nhnacademy.bookstore.shipment.repository.CarrierRepository;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepository;
import com.nhnacademy.bookstore.shipment.repository.ShipmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private CarrierRepository carrierRepository;

    @Mock
    private ShipmentPolicyRepository shipmentPolicyRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShipmentMapper shipmentMapper;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Test
    @DisplayName("배송 생성 테스트")
    void createShipment() {
        LocalDateTime shippingDate = LocalDateTime.now();
        LocalDateTime deliveryDate = shippingDate.plusDays(2);

        ShipmentRequestDto requestDto = new ShipmentRequestDto(1L, 1L, 1L, "빠른 배송 요청", shippingDate, deliveryDate, "12345");
        Carrier carrier = new Carrier(1L, "핑핑배송", "010-1234-5678", "carrier@example.com", "https://example.com");
        ShipmentPolicy policy = new ShipmentPolicy(1L, "정책 이름", 10000, true, null, null, 5000, true);
        Order order = new Order();
        Shipment savedShipment = new Shipment(1L, carrier, policy, order, "빠른 배송 요청", shippingDate, deliveryDate, "12345");
        ShipmentResponseDto responseDto = new ShipmentResponseDto(1L, 1L, 1L, 1L, "빠른 배송 요청", shippingDate, deliveryDate, "12345");

        when(carrierRepository.findById(requestDto.carrierId())).thenReturn(Optional.of(carrier));
        when(shipmentPolicyRepository.findById(requestDto.shipmentPolicyId())).thenReturn(Optional.of(policy));
        when(orderRepository.findById(requestDto.orderId())).thenReturn(Optional.of(order));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(savedShipment);
        when(shipmentMapper.toShipmentResponseDto(savedShipment)).thenReturn(responseDto);

        ShipmentResponseDto result = shipmentService.createShipment(requestDto);

        assertEquals("12345", result.trackingNumber());
        assertEquals(1L, result.carrierId());
    }

    @Test
    @DisplayName("특정 배송 조회 테스트")
    void getShipment() {
        LocalDateTime shippingDate = LocalDateTime.now();
        LocalDateTime deliveryDate = shippingDate.plusDays(2);

        Carrier carrier = new Carrier(1L, "핑핑배송", "010-1234-5678", "carrier@example.com", "https://example.com");
        ShipmentPolicy policy = new ShipmentPolicy(1L, "정책 이름", 10000, true, null, null, 5000, true);
        Order order = new Order();
        Shipment shipment = new Shipment(1L, carrier, policy, order, "빠른 배송 요청", shippingDate, deliveryDate, "12345");
        ShipmentResponseDto responseDto = new ShipmentResponseDto(1L, 1L, 1L, 1L, "빠른 배송 요청", shippingDate, deliveryDate, "12345");

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        when(shipmentMapper.toShipmentResponseDto(shipment)).thenReturn(responseDto);

        ShipmentResponseDto result = shipmentService.getShipment(1L);

        assertEquals(1L, result.shipmentId());
        assertEquals("12345", result.trackingNumber());
    }

    @Test
    @DisplayName("모든 배송 조회 테스트")
    void getAllShipments() {
        ShipmentResponseDto responseDto1 = new ShipmentResponseDto(1L, 1L, 1L, 1L, "빠른 배송 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");
        ShipmentResponseDto responseDto2 = new ShipmentResponseDto(2L, 1L, 1L, 1L, "일반 배송 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(3), "67890");

        when(shipmentRepository.findAllShipmentDtos()).thenReturn(List.of(responseDto1, responseDto2));

        List<ShipmentResponseDto> responseList = shipmentService.getAllShipments();

        assertEquals(2, responseList.size());
        assertEquals("12345", responseList.get(0).trackingNumber());
        assertEquals("67890", responseList.get(1).trackingNumber());
    }

    @Test
    @DisplayName("배송 완료된 정보 조회 테스트")
    void getCompletedShipments() {
        LocalDateTime now = LocalDateTime.now();
        ShipmentResponseDto responseDto = new ShipmentResponseDto(1L, 1L, 1L, 1L, "빠른 배송 요청", now.minusDays(3), now.minusDays(1), "12345");

        when(shipmentRepository.findCompletedShipmentDtos(any(LocalDateTime.class))).thenReturn(List.of(responseDto));

        List<ShipmentResponseDto> completedShipments = shipmentService.getCompletedShipments();

        assertEquals(1, completedShipments.size());
        assertEquals("12345", completedShipments.get(0).trackingNumber());
    }

    @Test
    @DisplayName("배송 미완료 정보 조회 테스트")
    void getPendingShipments() {
        LocalDateTime now = LocalDateTime.now();
        ShipmentResponseDto responseDto = new ShipmentResponseDto(1L, 1L, 1L, 1L, "일반 배송 요청", now.plusDays(1), now.plusDays(3), "67890");

        when(shipmentRepository.findPendingShipmentDtos(any(LocalDateTime.class))).thenReturn(List.of(responseDto));

        List<ShipmentResponseDto> pendingShipments = shipmentService.getPendingShipments();

        assertEquals(1, pendingShipments.size());
        assertEquals("67890", pendingShipments.get(0).trackingNumber());
    }

    @Test
    @DisplayName("배송 수정 테스트")
    void updateShipment() {
        // given
        ShipmentRequestDto requestDto = new ShipmentRequestDto(1L, 1L, 1L, "업데이트된 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");
        Carrier carrier = new Carrier(1L, "핑핑배송", "010-1234-5678", "carrier@example.com", "https://example.com");
        ShipmentPolicy policy = new ShipmentPolicy(1L, "정책 이름", 10000, true, null, null, 5000, true);
        Order order = new Order();
        Shipment existingShipment = new Shipment(1L, carrier, policy, order, "빠른 배송 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");
        ShipmentResponseDto responseDto = new ShipmentResponseDto(1L, 1L, 1L, 1L, "업데이트된 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");

        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));
        when(shipmentPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(existingShipment));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(existingShipment);
        when(shipmentMapper.toShipmentResponseDto(existingShipment)).thenReturn(responseDto);

        // when
        ShipmentResponseDto result = shipmentService.updateShipment(1L, requestDto);

        // then
        assertEquals("업데이트된 요청", result.requirement());
        assertEquals("12345", result.trackingNumber());
    }

    @Test
    @DisplayName("배송 삭제 테스트")
    void deleteShipment() {
        // given
        Carrier carrier = new Carrier(1L, "핑핑배송", "010-1234-5678", "carrier@example.com", "https://example.com");
        ShipmentPolicy policy = new ShipmentPolicy(1L, "정책 이름", 10000, true, null, null, 5000, true);
        Order order = new Order();
        Shipment shipment = new Shipment(1L, carrier, policy, order, "빠른 배송 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        doNothing().when(shipmentRepository).delete(shipment);

        // when
        shipmentService.deleteShipment(1L);

        // then
        verify(shipmentRepository, times(1)).delete(shipment);
    }
}
