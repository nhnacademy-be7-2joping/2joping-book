package com.nhnacademy.bookstore.bookset.book.service;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    BookResponseDto createBook(BookCreateRequestDto request);
}
