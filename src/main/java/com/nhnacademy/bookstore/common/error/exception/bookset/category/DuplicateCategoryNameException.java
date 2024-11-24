package com.nhnacademy.bookstore.common.error.exception.bookset.category;

import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;

public class DuplicateCategoryNameException extends ConflictException {
    public static final String MESSAGE = "이미 사용 중인 카테고리 이름입니다.";
    public DuplicateCategoryNameException () {
        super(MESSAGE);
    }
}
