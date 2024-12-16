package com.nhnacademy.bookstore.common.error.exception.imageset;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class ImageNotFoundException extends NotFoundException {

    public ImageNotFoundException(String message) {
        super(message);
    }
}
