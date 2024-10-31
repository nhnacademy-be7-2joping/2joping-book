package com.nhnacademy.bookstore.bookset.publisher.exception;

public class PublisherNotFoundException extends RuntimeException {
    public PublisherNotFoundException(String message) {
        super(message);
    }
}
