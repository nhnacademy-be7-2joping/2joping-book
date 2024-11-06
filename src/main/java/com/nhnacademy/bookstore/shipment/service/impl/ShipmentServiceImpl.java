package com.nhnacademy.bookstore.shipment.service.impl;

import com.nhnacademy.bookstore.common.error.exception.orderset.order.OrderNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.shipment.CarrierNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.shipment.ShipmentNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.shipment.ShipmentPolicyNotFoundException;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Shipment;
import com.nhnacademy.bookstore.shipment.mapper.ShipmentMapper;
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

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final CarrierRepository carrierRepository;
    private final ShipmentPolicyRepository shipmentPolicyRepository;
    private final OrderRepository orderRepository;
    private final ShipmentMapper shipmentMapper;

    @Override
    @Transactional
    public ShipmentResponseDto createShipment(ShipmentRequestDto requestDto) {
        Carrier carrier = carrierRepository.findById(requestDto.carrierId())
                .orElseThrow(CarrierNotFoundException::new);
        ShipmentPolicy shipmentPolicy = shipmentPolicyRepository.findById(requestDto.shipmentPolicyId())
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        Order order = orderRepository.findById(requestDto.orderId())
                .orElseThrow(OrderNotFoundException::new);

        Shipment shipment = new Shipment(
                null,
                carrier,
                shipmentPolicy,
                order,
                requestDto.requirement(),
                requestDto.shippingDate(),
                requestDto.deliveryDate(),
                requestDto.trackingNumber()
        );

        Shipment savedShipment = shipmentRepository.save(shipment);
        return shipmentMapper.toShipmentResponseDto(savedShipment);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponseDto getShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(ShipmentNotFoundException::new);
        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponseDto> getAllShipments() {
        return shipmentRepository.findAllShipmentDtos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponseDto> getCompletedShipments() {
        LocalDateTime now = LocalDateTime.now();
        return shipmentRepository.findCompletedShipmentDtos(now);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponseDto> getPendingShipments() {
        LocalDateTime now = LocalDateTime.now();
        return shipmentRepository.findPendingShipmentDtos(now);
    }


    @Override
    @Transactional
    public ShipmentResponseDto updateShipment(Long shipmentId, ShipmentRequestDto requestDto) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(ShipmentNotFoundException::new);
        Carrier carrier = carrierRepository.findById(requestDto.carrierId())
                .orElseThrow(CarrierNotFoundException::new);
        ShipmentPolicy shipmentPolicy = shipmentPolicyRepository.findById(requestDto.shipmentPolicyId())
                .orElseThrow(ShipmentPolicyNotFoundException::new);
        Order order = orderRepository.findById(requestDto.orderId())
                .orElseThrow(OrderNotFoundException::new);

        shipment.toEntity(requestDto, carrier, shipmentPolicy, order);
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return shipmentMapper.toShipmentResponseDto(updatedShipment);
    }

    @Override
    @Transactional
    public void deleteShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(ShipmentNotFoundException::new);
        shipmentRepository.delete(shipment);
    }
}
