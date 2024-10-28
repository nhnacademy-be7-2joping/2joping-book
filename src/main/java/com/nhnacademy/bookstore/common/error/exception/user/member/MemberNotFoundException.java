package com.nhnacademy.bookstore.common.error.exception.user.member;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
