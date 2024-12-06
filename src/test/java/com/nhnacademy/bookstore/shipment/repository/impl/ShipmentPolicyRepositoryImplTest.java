package com.nhnacademy.bookstore.shipment.repository.impl;

import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShippingFeeResponseDto;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepository;
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
class ShipmentPolicyRepositoryImplTest {

    @Autowired
    private ShipmentPolicyRepositoryImpl shipmentPolicyRepository;

    @Autowired
    private ShipmentPolicyRepository shipmentPolicyJpaRepository;

    @BeforeEach
    void setUp() {
        ShipmentPolicy policy1 = new ShipmentPolicy(null, "Standard Shipping", 30000, false, LocalDateTime.now(), LocalDateTime.now(), 2500, true);
        ShipmentPolicy policy2 = new ShipmentPolicy(null, "Member Exclusive Free Shipping", 50000, true, LocalDateTime.now(), LocalDateTime.now(), 0, true);
        ShipmentPolicy policy3 = new ShipmentPolicy(null, "Inactive Policy", 10000, false, LocalDateTime.now(), LocalDateTime.now(), 5000, false);

        shipmentPolicyJpaRepository.saveAll(List.of(policy1, policy2, policy3));
    }

    @Test
    @DisplayName("활성화된 배송 정책 조회 테스트")
    void findActiveShipmentPolicies() {
        // when
        List<ShipmentPolicyResponseDto> activePolicies = shipmentPolicyRepository.findActiveShipmentPolicies();

        // then
        assertThat(activePolicies).hasSize(2);
        assertThat(activePolicies.get(0).name()).isEqualTo("Standard Shipping");
        assertThat(activePolicies.get(1).name()).isEqualTo("Member Exclusive Free Shipping");
    }

    @Test
    @DisplayName("활성화된 배송비 조회 테스트 - 로그인 사용자")
    void findActiveShippingFeeForLoggedInUser() {
        // when
        List<ShippingFeeResponseDto> shippingFees = shipmentPolicyRepository.findActiveShippingFee(true);

        // then
        assertThat(shippingFees).hasSize(1);
        assertThat(shippingFees.getFirst().minOrderAmount()).isEqualTo(50000);
        assertThat(shippingFees.getFirst().shippingFee()).isZero();
    }

    @Test
    @DisplayName("활성화된 배송비 조회 테스트 - 비로그인 사용자")
    void findActiveShippingFeeForGuest() {
        // when
        List<ShippingFeeResponseDto> shippingFees = shipmentPolicyRepository.findActiveShippingFee(false);

        // then
        assertThat(shippingFees).hasSize(1);
        assertThat(shippingFees.getFirst().minOrderAmount()).isEqualTo(30000);
        assertThat(shippingFees.getFirst().shippingFee()).isEqualTo(2500);
    }
}
