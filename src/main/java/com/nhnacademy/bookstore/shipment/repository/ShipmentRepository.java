package com.nhnacademy.bookstore.shipment.repository;

import com.nhnacademy.bookstore.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long>, ShipmentRepositoryCustom {
}
