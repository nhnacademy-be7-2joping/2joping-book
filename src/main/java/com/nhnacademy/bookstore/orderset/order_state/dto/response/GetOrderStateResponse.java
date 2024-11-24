package com.nhnacademy.bookstore.orderset.order_state.dto.response;

import com.nhnacademy.bookstore.orderset.order_state.entity.OrderState;
import com.nhnacademy.bookstore.orderset.order_state.entity.vo.OrderStateType;

public record GetOrderStateResponse(

        Long orderStateId,
        OrderStateType name
) {
    public static GetOrderStateResponse from(OrderState orderState) {
        return new GetOrderStateResponse(
                orderState.getOrderStateId(),
                orderState.getName()
        );
    }
}
