package com.nhnacademy.bookstore.orderset.order_state.repository;

import com.nhnacademy.bookstore.orderset.order_state.entity.OrderState;
import com.nhnacademy.bookstore.orderset.order_state.entity.vo.OrderStateType;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderStateRepository extends JpaRepository<OrderState, Long> {

    Optional<OrderState> findByOrderStateId(Long id);
    List<OrderState> findAllByOrderByOrderDateDesc();
}
