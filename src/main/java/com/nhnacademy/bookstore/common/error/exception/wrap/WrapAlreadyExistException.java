package com.nhnacademy.bookstore.common.error.exception.wrap;

import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;

public class WrapAlreadyExistException extends ConflictException {
    public static final String MESSAGE = "포장 상품이 이미 존재합니다.";
    public WrapAlreadyExistException() {
        super(MESSAGE);
    }
}
