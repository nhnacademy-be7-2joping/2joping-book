package com.nhnacademy.bookstore.shipment.repository;

import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentPolicyRepository extends JpaRepository<ShipmentPolicy, Long>, ShipmentPolicyRepositoryCustom {
}
