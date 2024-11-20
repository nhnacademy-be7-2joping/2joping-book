package com.nhnacademy.bookstore.bookset.book.service;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface BookService {
    Page<BookSimpleResponseDto> getAllBooks(Pageable pageable);
    BookResponseDto getBookById(Long bookId);
    Page<BookSimpleResponseDto> getBooksByCategoryId(Pageable pageable, Long categoryId);
    Page<BookSimpleResponseDto> getBooksByContributorId(Pageable pageable, Long contributorId);
}
