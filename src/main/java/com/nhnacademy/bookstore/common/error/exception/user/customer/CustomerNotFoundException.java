package com.nhnacademy.bookstore.common.error.exception.user.customer;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {
    /**
     * CustomerNotFoundException 생성자.
     *
     * @param customerId   조회에 실패한 고객 ID
     * @param redirectType 리다이렉션 타입 (예: REDIRECT, FORWARD 등)
     * @param url          리다이렉션 대상 URL
     */
    public CustomerNotFoundException(Long customerId, RedirectType redirectType, String url) {
        super(String.format("Cannot find a customer with id %d", customerId), redirectType, url);
    }

    public CustomerNotFoundException(Long customerId) {
        super(String.format("Cannot find a customer with id %d", customerId), RedirectType.REDIRECT, "/");
    }
}
