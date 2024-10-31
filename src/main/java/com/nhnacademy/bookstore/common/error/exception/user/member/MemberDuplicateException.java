package com.nhnacademy.bookstore.common.error.exception.user.member;

import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;

public class MemberDuplicateException extends ConflictException {
    public MemberDuplicateException(String message) {
        super(message);
    }
}
