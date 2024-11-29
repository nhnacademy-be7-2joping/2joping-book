package com.nhnacademy.bookstore.common.error.exception.bookset.book;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;

public class BookStockOutException extends BadRequestException {
    public BookStockOutException(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }
}
