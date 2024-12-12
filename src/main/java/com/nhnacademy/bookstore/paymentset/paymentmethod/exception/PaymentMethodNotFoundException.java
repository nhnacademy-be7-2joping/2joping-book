package com.nhnacademy.bookstore.paymentset.paymentmethod.exception;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class PaymentMethodNotFoundException extends NotFoundException {
    public PaymentMethodNotFoundException(String method) {
        super("결제수단 " + method + "이 존재하지 않습니다.");
    }
}
