package com.nhnacademy.bookstore.common.error.exception.user.customer;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;

public class NonMemberValidationFailed extends BadRequestException {
    /**
     * 리다이렉트 타입과 URL을 포함한 생성자.
     *
     * @param message 예외 메시지
     * @param redirectType 리다이렉트 타입
     * @param url 리다이렉트될 URL
     */
    public NonMemberValidationFailed(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }
}
