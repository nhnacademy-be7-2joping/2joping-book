package com.nhnacademy.bookstore.common.error.exception.orderset.order_detail;

public class OrderDetailNotFoundException extends RuntimeException {
    public OrderDetailNotFoundException(String message) {
        super(message);
    }
}
