package com.nhnacademy.bookstore.shipment.repository;

import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ShipmentPolicyRepository extends JpaRepository<ShipmentPolicy, Long> {
    Collection<ShipmentPolicy> findByIsActiveTrue();
}
