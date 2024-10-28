package com.nhnacademy.bookstore.common.error.exception.base;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}