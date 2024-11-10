package com.nhnacademy.bookstore.bookset.book.controller;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookContributorResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    private BookSimpleResponseDto bookSimpleDto;
    private BookResponseDto bookResponseDto;
    private Page<BookSimpleResponseDto> bookPage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // BookContributorResponseDto 리스트 생성
        List<BookContributorResponseDto> contributors = List.of(
                new BookContributorResponseDto(1L, "Contributor 1", 1L, "Author")
        );

        // BookSimpleResponseDto 및 BookResponseDto 객체 초기화
        bookSimpleDto = new BookSimpleResponseDto(
                1L, "thumbnail1", "Book Title 1", 15000, "Publisher 1", 20000, true,
                contributors, List.of("Category 1", "Category 2")
        );

        bookPage = new PageImpl<>(List.of(bookSimpleDto));

        bookResponseDto = new BookResponseDto(
                1L, "Publisher 1", "Book Title 1", "Description", LocalDate.of(2023, 10, 29),
                "1234567890123", 20000, 15000, true, true, 10, 0, 0,
                contributors, List.of("Category 1", "Category 2"), "thumbnail1"
        );
    }

    @Test
    @DisplayName("전체 도서 조회")
    void testGetAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);

        ResponseEntity<Page<BookSimpleResponseDto>> response = bookController.getAllBooks(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(bookSimpleDto, response.getBody().getContent().get(0));
    }

    @Test
    @DisplayName("카테고리 id로 도서 조회")
    void testGetBooksByCategoryId() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookService.getBooksByCategoryId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        ResponseEntity<Page<BookSimpleResponseDto>> response = bookController.getBooksByCategoryId(1L, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(bookSimpleDto, response.getBody().getContent().get(0));
    }

    @Test
    @DisplayName("기여자 id로 도서 조회")
    void testGetBooksByContributorId() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookService.getBooksByContributorId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        ResponseEntity<Page<BookSimpleResponseDto>> response = bookController.getBooksByContributorId(1L, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(bookSimpleDto, response.getBody().getContent().get(0));
    }

    @Test
    @DisplayName("도서 id로 특정 도서 하나 조회")
    void testGetBookByBookId() {
        when(bookService.getBookById(anyLong())).thenReturn(bookResponseDto);

        ResponseEntity<BookResponseDto> response = bookController.getBookByBookId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookResponseDto, response.getBody());
    }
}