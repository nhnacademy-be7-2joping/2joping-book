package com.nhnacademy.bookstore.bookset.tag.exception;

public class TagNotFoundException extends RuntimeException{
    public TagNotFoundException() {
        super("태그를 찾을 수 없습니다");
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
