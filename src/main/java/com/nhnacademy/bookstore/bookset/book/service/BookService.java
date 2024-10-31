package com.nhnacademy.bookstore.bookset.book.service;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface BookService {
    BookResponseDto createBook(BookCreateRequestDto request);
    Page<BookSimpleResponseDto> getAllBooks(Pageable pageable);
    Optional<BookResponseDto> getbookById(Long bookId);
    Page<BookSimpleResponseDto> getBooksByCategoryId(Pageable pageable, Long categoryId);
}
