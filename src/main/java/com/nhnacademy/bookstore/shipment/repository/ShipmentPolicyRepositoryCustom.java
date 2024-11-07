package com.nhnacademy.bookstore.shipment.repository;

import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import java.util.List;

public interface ShipmentPolicyRepositoryCustom {
    List<ShipmentPolicyResponseDto> findActiveShipmentPolicies();
}
