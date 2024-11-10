package com.nhnacademy.bookstore.common.error.exception.base;

public class UnauthorizedException  extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
