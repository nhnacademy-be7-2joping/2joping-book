package com.nhnacademy.bookstore.orderset.orderstate.service;

import com.nhnacademy.bookstore.common.error.exception.orderset.orderstate.OrderStateNotFoundException;
import com.nhnacademy.bookstore.orderset.orderstate.dto.request.CreateOrderStateRequest;
import com.nhnacademy.bookstore.orderset.orderstate.dto.request.UpdateOrderStateRequest;
import com.nhnacademy.bookstore.orderset.orderstate.dto.response.GetOrderStateResponse;
import com.nhnacademy.bookstore.orderset.orderstate.entity.OrderState;
import com.nhnacademy.bookstore.orderset.orderstate.repository.OrderStateRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderStateService {

    private final OrderStateRepository orderStateRepository;

    public Long createOrderState(@Valid CreateOrderStateRequest request) {
        OrderState orderState = OrderState.builder()
                .name(request.name())
                .build();
        return orderStateRepository.save(orderState).getOrderStateId();
    }

    @Transactional(readOnly = true)
    public Long updateOrderState(Long orderStateId, @Valid UpdateOrderStateRequest request) {
        OrderState orderState = orderStateRepository.findByOrderStateId(orderStateId)
                .orElseThrow(OrderStateNotFoundException::new);

        orderState.updateName(request.newName());

        return orderStateRepository.save(orderState).getOrderStateId();
    }

    @Transactional(readOnly = true)
    public GetOrderStateResponse getOrderState(Long orderStateId) {
        OrderState orderState = orderStateRepository.findByOrderStateId(orderStateId)
                .orElseThrow((OrderStateNotFoundException::new));

        return GetOrderStateResponse.from(orderState);
    }

    @Transactional(readOnly = true)
    public OrderState getWaitingState() {
        OrderState orderState =
                orderStateRepository.findByOrderStateId(0L).orElseThrow((OrderStateNotFoundException::new));

        return orderState;
    }

    public void deleteOrderState(Long orderStateId) {
        OrderState orderState = orderStateRepository.findByOrderStateId(orderStateId)
                .orElseThrow(OrderStateNotFoundException::new);
        orderStateRepository.delete(orderState);
    }
}
