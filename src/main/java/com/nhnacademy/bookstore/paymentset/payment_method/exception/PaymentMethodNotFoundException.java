package com.nhnacademy.bookstore.paymentset.payment_method.exception;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import com.nhnacademy.bookstore.paymentset.payment_method.enums.PaymentMethodType;

public class PaymentMethodNotFoundException extends NotFoundException {
    public PaymentMethodNotFoundException(String method) {
        super("결제수단 " + method + "이 존재하지 않습니다.");
    }
}
