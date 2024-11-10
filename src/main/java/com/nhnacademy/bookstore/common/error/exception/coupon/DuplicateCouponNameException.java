package com.nhnacademy.bookstore.common.error.exception.coupon;


import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;

public class DuplicateCouponNameException extends BadRequestException {

    public DuplicateCouponNameException(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }
}
