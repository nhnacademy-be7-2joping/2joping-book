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

/**
 * ShipmentService 구현체 클래스.
 * 배송 정보의 생성, 조회, 수정 및 삭제 기능을 제공합니다.
 *
 * @author 양준하
 */
@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final CarrierRepository carrierRepository;
    private final ShipmentPolicyRepository shipmentPolicyRepository;
    private final OrderRepository orderRepository;
    private final ShipmentMapper shipmentMapper;

    /**
     * 새로운 배송 정보를 생성하는 메서드입니다.
     *
     * @param requestDto 생성할 배송 정보가 담긴 DTO
     * @return 생성된 배송 정보의 ShipmentResponseDto
     * @throws CarrierNotFoundException 배송 업체를 찾을 수 없을 경우 발생
     * @throws ShipmentPolicyNotFoundException 배송 정책을 찾을 수 없을 경우 발생
     * @throws OrderNotFoundException 주문을 찾을 수 없을 경우 발생
     */
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

    /**
     * 특정 ID의 배송 정보를 조회하는 메서드입니다.
     *
     * @param shipmentId 조회할 배송의 ID
     * @return 조회된 배송 정보의 ShipmentResponseDto
     * @throws ShipmentNotFoundException 배송을 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional(readOnly = true)
    public ShipmentResponseDto getShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(ShipmentNotFoundException::new);
        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    /**
     * 모든 배송 정보를 조회하는 메서드입니다.
     *
     * @return 모든 배송 정보의 ShipmentResponseDto 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponseDto> getAllShipments() {
        return shipmentRepository.findAllShipmentDtos();
    }

    /**
     * 배송 완료된 정보를 조회하는 메서드입니다.
     *
     * @return 배송 완료된 정보의 ShipmentResponseDto 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponseDto> getCompletedShipments() {
        LocalDateTime now = LocalDateTime.now();
        return shipmentRepository.findCompletedShipmentDtos(now);
    }

    /**
     * 배송이 완료되지 않은 정보를 조회하는 메서드입니다.
     *
     * @return 배송 미완료 정보의 ShipmentResponseDto 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponseDto> getPendingShipments() {
        LocalDateTime now = LocalDateTime.now();
        return shipmentRepository.findPendingShipmentDtos(now);
    }

    /**
     * 특정 ID의 배송 정보를 수정하는 메서드입니다.
     *
     * @param shipmentId 수정할 배송의 ID
     * @param requestDto 수정할 배송 정보가 담긴 DTO
     * @return 수정된 배송 정보의 ShipmentResponseDto
     * @throws ShipmentNotFoundException 배송을 찾을 수 없을 경우 발생
     * @throws CarrierNotFoundException 배송 업체를 찾을 수 없을 경우 발생
     * @throws ShipmentPolicyNotFoundException 배송 정책을 찾을 수 없을 경우 발생
     * @throws OrderNotFoundException 주문을 찾을 수 없을 경우 발생
     */
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

    /**
     * 특정 ID의 배송 정보를 삭제하는 메서드입니다.
     *
     * @param shipmentId 삭제할 배송의 ID
     * @throws ShipmentNotFoundException 배송을 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public void deleteShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(ShipmentNotFoundException::new);
        shipmentRepository.delete(shipment);
    }
}
