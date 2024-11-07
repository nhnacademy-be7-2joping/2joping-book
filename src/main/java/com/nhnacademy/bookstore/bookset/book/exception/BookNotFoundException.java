package com.nhnacademy.bookstore.bookset.book.exception;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
