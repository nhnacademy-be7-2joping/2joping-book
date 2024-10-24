package com.nhnacademy.bookstore.bookset.contributor.exception;

public class NotFoundContributorException extends RuntimeException {
    public static final String MESSAGE = "해당 기여자가 없습니다.";
    public NotFoundContributorException() {
        super(MESSAGE);
    }
}
