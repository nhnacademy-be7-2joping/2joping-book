package com.nhnacademy.bookstore.bookset.contributor.exception;

public class NotFoundContributorRoleException extends RuntimeException {
    public static final String MESSAGE = "해당 기여자 역할이 없습니다.";
    public NotFoundContributorRoleException() {
        super(MESSAGE);
    }
}
