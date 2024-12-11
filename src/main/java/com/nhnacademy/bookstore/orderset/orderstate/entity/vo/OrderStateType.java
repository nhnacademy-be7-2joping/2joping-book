package com.nhnacademy.bookstore.orderset.orderstate.entity.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStateType {
    WAITING(0, "대기"),
    SHIPPING(1, "배송중"),
    COMPLETED(2, "완료"),
    RETURN_PENDING(3, "반품대기"),
    RETURNED(4, "반품"),
    CANCELLED(5, "주문취소");

    private final int code;
    private final String description;

    public static OrderStateType valueOf(int code) {
        for (OrderStateType state : values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        throw new IllegalArgumentException("잘못된 상태 코드를 입력하였습니다 : " + code);
    }
}
