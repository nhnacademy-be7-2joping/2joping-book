package com.nhnacademy.bookstore.orderset.order.repository;

import com.nhnacademy.bookstore.orderset.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderId(Long orderId);

    @Query("SELECT o FROM Order o WHERE o.orderStateId = :orderStateId")
    Optional<Order> findByOrderStateId(@Param("orderStateId") Long orderStateId);
}
