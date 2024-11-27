package com.nhnacademy.bookstore.common.error.exception.bookset.book;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class BookNotFoundException extends NotFoundException {
  public static final String MESSAGE = "해당 도서가 없습니다.";
  public BookNotFoundException(String message) {
    super(message);
  }

  public BookNotFoundException() {
    super(MESSAGE);
  }
}
