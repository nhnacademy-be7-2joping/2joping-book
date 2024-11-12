package com.nhnacademy.bookstore.common.error.exception.orderset.order;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 주문 정보가 없습니다.";
    public OrderNotFoundException() {
        super(MESSAGE);
    }
}
