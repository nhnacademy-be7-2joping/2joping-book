package com.nhnacademy.bookstore.common.error.exception.base;

public class ConflictException extends RuntimeException{
    public ConflictException(String message) {
        super(message);
    }
}