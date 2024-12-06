package com.nhnacademy.bookstore.common.error.exception.bookset.category;

/**
 * 카테고리 Id가 null일 경우 발생시키는 에러
 *
 * @author : 이초은
 * @date : 2024-12-06
 */
public class CategoryIdNullException extends RuntimeException {
    public static final String MESSAGE = "카테고리 Id는 null이 될 수 없습니다.";
    public CategoryIdNullException() {
        super(MESSAGE);
    }
}
