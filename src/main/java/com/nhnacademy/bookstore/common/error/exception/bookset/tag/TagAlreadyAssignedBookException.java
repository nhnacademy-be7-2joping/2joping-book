package com.nhnacademy.bookstore.common.error.exception.bookset.tag;

import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;

public class TagAlreadyAssignedBookException extends ConflictException {
    public static final String MESSAGE = "태그가 이미 책에 할당되어 있습니다.";
    public TagAlreadyAssignedBookException() {
        super(MESSAGE);
    }
}
