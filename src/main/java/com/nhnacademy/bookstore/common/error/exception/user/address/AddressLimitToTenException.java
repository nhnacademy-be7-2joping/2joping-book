package com.nhnacademy.bookstore.common.error.exception.user.address;

import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;

public class AddressLimitToTenException extends ConflictException {
    public AddressLimitToTenException(String message) {
        super(message);
    }
}
