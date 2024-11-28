package com.nhnacademy.bookstore.orderset.order.repository;

import com.nhnacademy.bookstore.orderset.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    Optional<Order> findByOrderId(Long orderId);
}
