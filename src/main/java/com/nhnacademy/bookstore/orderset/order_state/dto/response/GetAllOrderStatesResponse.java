package com.nhnacademy.bookstore.orderset.order_state.dto.response;

import com.nhnacademy.bookstore.orderset.order_state.entity.OrderState;
import com.nhnacademy.bookstore.orderset.order_state.entity.vo.OrderStateType;

import java.util.List;

public record GetAllOrderStatesResponse(

        List<OrderStateResponse> orderStates
) {
    public static GetAllOrderStatesResponse from(List<OrderState> orderStates) {
        List<OrderStateResponse> orderStateResponses = orderStates.stream()
                .map(orderState -> new OrderStateResponse(orderState.getOrderStateId(), orderState.getName()))
                .toList();

        return new GetAllOrderStatesResponse(orderStateResponses);
    }

    private record OrderStateResponse(
            Long orderStatId,
            OrderStateType name
    ) {
    }
}
