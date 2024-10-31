package com.nhnacademy.bookstore.bookset.publisher.exception;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class PublisherNotFoundException extends NotFoundException {
    public PublisherNotFoundException(String message) {
        super(message);
    }
}
