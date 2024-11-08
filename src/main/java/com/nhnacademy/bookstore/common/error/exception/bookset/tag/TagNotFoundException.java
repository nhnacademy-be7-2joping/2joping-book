package com.nhnacademy.bookstore.common.error.exception.bookset.tag;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class TagNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 태그가 없습니다.";
    public TagNotFoundException() {
        super(MESSAGE);
    }
}
