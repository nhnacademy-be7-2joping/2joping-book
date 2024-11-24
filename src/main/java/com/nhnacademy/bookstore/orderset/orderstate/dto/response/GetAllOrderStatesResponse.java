package com.nhnacademy.bookstore.orderset.orderstate.dto.response;

import com.nhnacademy.bookstore.orderset.orderstate.entity.OrderState;
import com.nhnacademy.bookstore.orderset.orderstate.entity.vo.OrderStateType;

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
