package com.nhnacademy.bookstore.common.error.exception.coupon;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;

public class CouponInvalidException extends BadRequestException {
    public CouponInvalidException(String message) {
        super(message, RedirectType.REDIRECT, "/");
    }
}
