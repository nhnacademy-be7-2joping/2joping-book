package com.nhnacademy.bookstore.orderset.orderstate.dto.request;

import com.nhnacademy.bookstore.orderset.orderstate.entity.vo.OrderStateType;
import jakarta.validation.constraints.NotNull;

public record CreateOrderStateRequest(

        @NotNull(message = "주문 상태가 존재하지 않습니다.")
        OrderStateType name
) {
}
