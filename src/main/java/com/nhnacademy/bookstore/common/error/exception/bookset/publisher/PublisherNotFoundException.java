package com.nhnacademy.bookstore.common.error.exception.bookset.publisher;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class PublisherNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 출판사가 없습니다.";
    public PublisherNotFoundException() {
        super(MESSAGE);
    }
}
