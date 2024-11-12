package com.nhnacademy.bookstore.common.error.exception.coupon;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class CouponPolicyNotFoundException extends NotFoundException {
    public CouponPolicyNotFoundException(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }
}
