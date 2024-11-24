package com.nhnacademy.bookstore.common.error.exception.bookset.category;

import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;

public class CannotDeactivateCategoryException extends BadRequestException {
    public static final String MESSAGE = "해당 부모 카테고리에 하위 카테고리가 존재하여 삭제할 수 없습니다.";
    public CannotDeactivateCategoryException () {
        super(MESSAGE);
    }
}
