package com.nhnacademy.bookstore.orderset.order_state.dto.request;

import com.nhnacademy.bookstore.orderset.order_state.entity.vo.OrderStateType;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStateRequest(

        @NotNull(message = "주문 상태가 존재하지 않습니다.")
        OrderStateType newName
) {
}