package com.nhnacademy.bookstore.common.error.exception.orderset.orderdetail;

public class OrderDetailNotFoundException extends RuntimeException {
    public OrderDetailNotFoundException(String message) {
        super(message);
    }
}
