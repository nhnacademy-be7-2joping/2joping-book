package com.nhnacademy.bookstore.common.error.exception.bookset.contributor;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class ContributorNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 기여자가 없습니다.";
    public ContributorNotFoundException() {
        super(MESSAGE);
    }
}
