package com.nhnacademy.bookstore.common.error.exception.point;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class PointTypeNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 포인트 타입이 없습니다.";

    public PointTypeNotFoundException() {
        super(MESSAGE);
    }
}

