package com.nhnacademy.bookstore.common.error.exception.user.member.tier;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class MemberTierNotFoundException extends NotFoundException {
    public MemberTierNotFoundException(String message) {
        super(message);
    }
}
