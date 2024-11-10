package com.nhnacademy.bookstore.common.error.exception.base;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}