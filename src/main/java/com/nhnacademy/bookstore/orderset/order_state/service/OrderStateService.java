package com.nhnacademy.bookstore.orderset.order_state.service;

import com.nhnacademy.bookstore.common.error.exception.orderset.orderstate.OrderStateNotFoundException;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.orderset.order_state.dto.request.CreateOrderStateRequest;
import com.nhnacademy.bookstore.orderset.order_state.dto.request.UpdateOrderStateRequest;
import com.nhnacademy.bookstore.orderset.order_state.dto.response.GetAllOrderStatesResponse;
import com.nhnacademy.bookstore.orderset.order_state.dto.response.GetOrderStateResponse;
import com.nhnacademy.bookstore.orderset.order_state.entity.OrderState;
import com.nhnacademy.bookstore.orderset.order_state.repository.OrderStateRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderStateService {

    private static final int INITIAL_PAGE_SIZE = 10;

    private final OrderStateRepository orderStateRepository;
    private final OrderRepository orderRepository;

    public Long createOrderState(@Valid CreateOrderStateRequest request) {
        OrderState orderState = OrderState.builder()
                .name(request.name())
                .build();
        return orderStateRepository.save(orderState).getOrderStateId();
    }

    public GetAllOrderStatesResponse getAllOrderStates() {
        List<OrderState> orderStates = orderStateRepository.findAllByOrderByOrderDateDesc();
        return GetAllOrderStatesResponse.from(orderStates);
    }

    public GetOrderStateResponse getOrderState(Long orderStateId) {
        Order order = orderRepository.findByOrderStateId(orderStateId)
                .orElseThrow((OrderStateNotFoundException::new));

        return GetOrderStateResponse.from(order);
    }

    public Long updateOrderState(Long orderStateId, @Valid UpdateOrderStateRequest request) {
        OrderState orderState = orderStateRepository.findByOrderStateId(orderStateId)
                .orElseThrow(OrderStateNotFoundException::new);

        orderState.updateName(request.newName());

        return orderStateRepository.save(orderState).getOrderStateId();
    }

    public void deleteOrderState(Long orderStateId) {
        OrderState orderState = orderStateRepository.findByOrderStateId(orderStateId)
                .orElseThrow(OrderStateNotFoundException::new);
        orderStateRepository.delete(orderState);
    }

}
