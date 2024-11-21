package com.nhnacademy.bookstore.common.error.exception.point;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class PointPolicyNotFoundException extends NotFoundException {
    public PointPolicyNotFoundException(String message) {
        super(message);
    }
}
