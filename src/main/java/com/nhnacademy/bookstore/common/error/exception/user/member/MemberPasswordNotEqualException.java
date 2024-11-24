package com.nhnacademy.bookstore.common.error.exception.user.member;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;

public class MemberPasswordNotEqualException extends BadRequestException {
    public MemberPasswordNotEqualException(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }
}
