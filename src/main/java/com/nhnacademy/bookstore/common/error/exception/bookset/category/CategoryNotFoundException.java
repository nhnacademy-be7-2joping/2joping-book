package com.nhnacademy.bookstore.common.error.exception.bookset.category;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

/**
 * 카테고리가 없을 경우 발생시키는 에러
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public class CategoryNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 카테고리가 없습니다.";
    public CategoryNotFoundException() {
        super(MESSAGE);
    }
}
