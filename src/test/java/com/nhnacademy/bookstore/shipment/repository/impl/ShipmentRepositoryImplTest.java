package com.nhnacademy.bookstore.shipment.repository.impl;

import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.orderset.orderstate.entity.OrderState;
import com.nhnacademy.bookstore.orderset.orderstate.entity.vo.OrderStateType;
import com.nhnacademy.bookstore.orderset.orderstate.repository.OrderStateRepository;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Shipment;
import com.nhnacademy.bookstore.shipment.entity.Carrier;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.customer.repository.CustomerRepository;
import com.nhnacademy.bookstore.shipment.repository.CarrierRepository;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepository;
import com.nhnacademy.bookstore.shipment.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ImportAutoConfiguration
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShipmentRepositoryImplTest {

    @Autowired
    private ShipmentRepositoryImpl shipmentRepository;

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private ShipmentPolicyRepository shipmentPolicyRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStateRepository orderStateRepository;

    @Autowired
    private ShipmentRepository shipmentJpaRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Shipment shipment;

    @BeforeEach
    void setUp() {
        // 고객 정보 생성 및 저장
        Customer customer = new Customer();
        customer.initializeCustomerFields("John Doe", "010-1234-5678", "john.doe@example.com");
        customer = customerRepository.save(customer);

        // 배송사 정보 생성 및 저장
        Carrier carrier = new Carrier(null, "Test Carrier", "010-1234-5678", "test@carrier.com", "https://carrier.com");
        carrier = carrierRepository.save(carrier);

        // 배송 정책 정보 생성 및 저장
        ShipmentPolicy policy = new ShipmentPolicy(null, "Standard Shipping", 30000, false, LocalDateTime.now(), LocalDateTime.now(), 2500, true);
        policy = shipmentPolicyRepository.save(policy);

        // 주문 상태 정보 생성 및 저장 (ID 명시적으로 설정)
        OrderState orderState = OrderState.builder()
                .name(OrderStateType.WAITING)
                .build();
        orderState.setOrderStateId(1L);
        orderState = orderStateRepository.save(orderState);

        // 주문 정보 생성 및 저장
        Order order = new Order(
                null, "ORD123", orderState, customer, null,
                LocalDateTime.now(), null, "Receiver",
                "12345", "Test Address", "Detail Address",
                0, 2500, 0, 11000, null
        );
        order = orderRepository.save(order);

        // 배송 정보 생성 및 저장
        shipment = new Shipment(
                null, carrier, policy, order, "Test Requirement",
                LocalDateTime.now().minusDays(1), LocalDateTime.now(), "TRACK123"
        );
        shipmentJpaRepository.save(shipment);
    }




    @Test
    @DisplayName("모든 배송 조회 테스트")
    void findAllShipmentDtos() {
        // when
        List<ShipmentResponseDto> shipments = shipmentRepository.findAllShipmentDtos();

        // then
        assertThat(shipments).hasSize(1);
        assertThat(shipments.getFirst().shipmentId()).isEqualTo(shipment.getShipmentId());
        assertThat(shipments.getFirst().trackingNumber()).isEqualTo("TRACK123");
    }

    @Test
    @DisplayName("완료된 배송 조회 테스트")
    void findCompletedShipmentDtos() {
        // when
        List<ShipmentResponseDto> shipments = shipmentRepository.findCompletedShipmentDtos(LocalDateTime.now());

        // then
        assertThat(shipments).hasSize(1);
        assertThat(shipments.getFirst().shipmentId()).isEqualTo(shipment.getShipmentId());
        assertThat(shipments.getFirst().deliveryDate()).isNotNull();
    }

    @Test
    @DisplayName("진행 중인 배송 조회 테스트")
    void findPendingShipmentDtos() {
        // given: 추가 테스트 데이터 생성
        Shipment newShipment = new Shipment(
                null, shipment.getCarrier(), shipment.getShipmentPolicy(),
                shipment.getOrder(), shipment.getRequirement(),
                shipment.getShippingDate(), null, "TRACK456"
        );
        shipmentJpaRepository.save(newShipment);

        // when: 진행 중인 배송 조회
        List<ShipmentResponseDto> shipments = shipmentRepository.findPendingShipmentDtos(LocalDateTime.now());

        // then: 추가된 배송 데이터가 조회되는지 확인
        assertThat(shipments).hasSize(1); // 진행 중인 배송은 1건이어야 함
        assertThat(shipments.getFirst().shipmentId()).isEqualTo(newShipment.getShipmentId());
        assertThat(shipments.getFirst().deliveryDate()).isNull();
    }

}
