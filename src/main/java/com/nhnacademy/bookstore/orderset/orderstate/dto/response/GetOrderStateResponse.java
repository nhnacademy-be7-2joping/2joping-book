package com.nhnacademy.bookstore.orderset.orderstate.dto.response;

import com.nhnacademy.bookstore.orderset.orderstate.entity.OrderState;
import com.nhnacademy.bookstore.orderset.orderstate.entity.vo.OrderStateType;

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
