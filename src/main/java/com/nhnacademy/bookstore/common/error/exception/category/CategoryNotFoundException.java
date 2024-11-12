package com.nhnacademy.bookstore.common.error.exception.category;

/**
 * 카테고리가 없을 경우 발생시키는 에러
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
