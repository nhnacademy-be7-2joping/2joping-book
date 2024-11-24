package com.nhnacademy.bookstore.orderset.orderstate.repository;

import com.nhnacademy.bookstore.orderset.orderstate.entity.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderStateRepository extends JpaRepository<OrderState, Long> {

    Optional<OrderState> findByOrderStateId(Long id);
    List<OrderState> findAllByOrderByOrderDateDesc();
}
