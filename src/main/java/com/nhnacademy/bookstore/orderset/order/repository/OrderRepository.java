package com.nhnacademy.bookstore.orderset.order.repository;

import com.nhnacademy.bookstore.orderset.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    Order findByOrderId(Long orderId);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderState.orderStateId = :orderStateId WHERE o.orderId = :orderId")
    int updateOrderStateByOrderIdAndOrderStateId(@Param("orderId") Long orderId, @Param("orderStateId") Long orderStateId);
}
