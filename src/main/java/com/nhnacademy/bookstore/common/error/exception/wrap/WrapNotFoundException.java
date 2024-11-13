package com.nhnacademy.bookstore.common.error.exception.wrap;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class WrapNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 포장상품이 없습니다.";
    public WrapNotFoundException() {
        super(MESSAGE);
    }
}
