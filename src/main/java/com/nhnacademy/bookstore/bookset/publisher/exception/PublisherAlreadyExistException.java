package com.nhnacademy.bookstore.bookset.publisher.exception;

import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;

public class PublisherAlreadyExistException extends ConflictException {
    public PublisherAlreadyExistException(String message) {
        super(message);
    }
}

