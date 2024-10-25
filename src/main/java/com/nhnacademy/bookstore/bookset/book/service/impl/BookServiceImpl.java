package com.nhnacademy.bookstore.bookset.book.service.impl;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    @Override
    public BookResponseDto createBook(BookCreateRequestDto request) {
        return new BookResponseDto();
    }
}
