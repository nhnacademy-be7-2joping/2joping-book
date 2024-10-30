package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.common.error.exception.orderset.order.OrderNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.shipment.CarrierNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.shipment.ShipmentNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.shipment.ShipmentPolicyNotFoundException;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Shipment;
import com.nhnacademy.bookstore.shipment.repository.ShipmentRepository;
import com.nhnacademy.bookstore.shipment.service.ShipmentService;
import com.nhnacademy.bookstore.shipment.entity.Carrier;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.shipment.repository.CarrierRepository;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepository;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final CarrierRepository carrierRepository;
    private final ShipmentPolicyRepository shipmentPolicyRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public ShipmentResponseDto createShipment(ShipmentRequestDto requestDto) {
        Carrier carrier = carrierRepository.findById(requestDto.getCarrierId())
                .orElseThrow(CarrierNotFoundException::new);
        ShipmentPolicy shipmentPolicy = shipmentPolicyRepository.findById(requestDto.getShipmentPolicyId())
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(OrderNotFoundException::new);

        Shipment shipment = new Shipment(
                null,
                carrier,
                shipmentPolicy,
                order,
                requestDto.getRequirement(),
                requestDto.getShippingDate(),
                requestDto.getDeliveryDate(),
                requestDto.getTrackingNumber()
        );

        Shipment savedShipment = shipmentRepository.save(shipment);
        return mapToResponseDto(savedShipment);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponseDto getShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(ShipmentNotFoundException::new);
        return mapToResponseDto(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponseDto> getAllShipments() {
        return shipmentRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponseDto> getCompletedShipments() {
        LocalDateTime now = LocalDateTime.now();
        return shipmentRepository.findAll().stream()
                .filter(shipment -> shipment.getDeliveryDate() != null && shipment.getDeliveryDate().isBefore(now))
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponseDto> getPendingShipments() {
        LocalDateTime now = LocalDateTime.now();
        return shipmentRepository.findAll().stream()
                .filter(shipment -> shipment.getDeliveryDate() == null || shipment.getDeliveryDate().isAfter(now))
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public ShipmentResponseDto updateShipment(Long shipmentId, ShipmentRequestDto requestDto) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(ShipmentNotFoundException::new);

        shipment.setRequirement(requestDto.getRequirement());
        shipment.setShippingDate(requestDto.getShippingDate());
        shipment.setDeliveryDate(requestDto.getDeliveryDate());
        shipment.setTrackingNumber(requestDto.getTrackingNumber());

        Shipment updatedShipment = shipmentRepository.save(shipment);
        return mapToResponseDto(updatedShipment);
    }

    @Override
    @Transactional
    public void deleteShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(ShipmentNotFoundException::new);
        shipmentRepository.delete(shipment);
    }

    private ShipmentResponseDto mapToResponseDto(Shipment shipment) {
        return new ShipmentResponseDto(
                shipment.getShipmentId(),
                shipment.getCarrier().getCarrierId(),
                shipment.getShipmentPolicy().getShipmentPolicyId(),
                shipment.getOrder().getOrderId(),
                shipment.getRequirement(),
                shipment.getShippingDate(),
                shipment.getDeliveryDate(),
                shipment.getTrackingNumber()
        );
    }
}
