package com.nhnacademy.bookstore.orderset.order_state.dto.response;

import com.nhnacademy.bookstore.orderset.order_state.entity.vo.OrderStateType;

public record GetOrderStateResponse(

        Long orderStateId,
        OrderStateType name
) {
}
