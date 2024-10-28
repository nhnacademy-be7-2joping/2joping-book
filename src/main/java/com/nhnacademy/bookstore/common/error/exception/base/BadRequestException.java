package com.nhnacademy.bookstore.common.error.exception.base;

public class BadRequestException  extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}