package com.nhnacademy.bookstore.bookset.tag.exception;

public class TagAlreadyExistException extends RuntimeException {

    public TagAlreadyExistException() {
        super("태그가 이미 존재합니다");
    }

    public TagAlreadyExistException(String message) {
        super(message);
    }
}
