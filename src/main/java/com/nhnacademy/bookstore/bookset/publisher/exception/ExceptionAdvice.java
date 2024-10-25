package com.nhnacademy.bookstore.bookset.publisher.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(PublisherAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handlePublisherAlreadyExistException(PublisherAlreadyExistException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().withNano(0).toString());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

    }

    @ExceptionHandler(PublisherNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePublisherNotFoundException(PublisherNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().withNano(0).toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
