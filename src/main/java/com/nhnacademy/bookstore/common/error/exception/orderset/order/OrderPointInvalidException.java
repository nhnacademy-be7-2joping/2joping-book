package com.nhnacademy.bookstore.common.error.exception.orderset.order;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;

public class OrderPointInvalidException extends BadRequestException {
    public OrderPointInvalidException(String message) {
        super(message, RedirectType.REDIRECT, "/");
    }
}
