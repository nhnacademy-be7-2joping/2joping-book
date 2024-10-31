package com.nhnacademy.bookstore.common.error.exception.user.member.status;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class MemberStatusNotFoundException extends NotFoundException {
    public MemberStatusNotFoundException(String message) {
        super(message);
    }
}
