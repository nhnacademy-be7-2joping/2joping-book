package com.nhnacademy.bookstore.common.error.exception.wrap;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class WrapImageNotFoundException extends NotFoundException {

    public WrapImageNotFoundException(String message) {
        super(message);
    }
}
