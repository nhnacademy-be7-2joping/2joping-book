package com.nhnacademy.bookstore.common.error.exception.review;

import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;

public class ReviewAlreadyExistException extends ConflictException {
    public static final String MESSAGE = "리뷰가 이미 존재합니다.";

    public ReviewAlreadyExistException(String message) {
        super(MESSAGE);
    }
}
