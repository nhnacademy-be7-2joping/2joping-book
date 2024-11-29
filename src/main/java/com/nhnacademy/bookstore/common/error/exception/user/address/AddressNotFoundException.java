package com.nhnacademy.bookstore.common.error.exception.user.address;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class AddressNotFoundException extends NotFoundException {

    public AddressNotFoundException(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }
}
