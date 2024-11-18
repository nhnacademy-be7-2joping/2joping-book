package com.nhnacademy.bookstore.common.error.exception.review;

import org.springframework.web.bind.MethodArgumentNotValidException;

public class RatingValueNotValidException extends RuntimeException  {
    private final int invalidValue;

    public RatingValueNotValidException(String message, int invalidValue) {
        super(message);
        this.invalidValue = invalidValue;
    }

    public int getInvalidValue() {
        return invalidValue;
    }
}
