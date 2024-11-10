package com.nhnacademy.bookstore.bookset.book.service;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookContributorResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.common.error.exception.bookset.book.BookNotFoundException;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.impl.BookServiceImpl;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    private BookSimpleResponseDto bookSimpleDto;
    private BookResponseDto bookResponseDto;
    private Page<BookSimpleResponseDto> bookPage;
    private List<BookContributorResponseDto> contributors;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // BookContributorResponseDto 리스트 초기화
        contributors = List.of(
                new BookContributorResponseDto(1L, "Contributor 1", 1L, "Author")
        );

        // BookSimpleResponseDto 및 Page 초기화
        bookSimpleDto = new BookSimpleResponseDto(
                1L, "thumbnail1", "Book Title 1", 15000, "Publisher 1", 20000, true,
                contributors, List.of("Category 1", "Category 2")
        );

        bookPage = new PageImpl<>(List.of(bookSimpleDto));

        // BookResponseDto 초기화
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
        when(bookRepository.findAllBooks(any(Pageable.class))).thenReturn(bookPage);

        Page<BookSimpleResponseDto> result = bookService.getAllBooks(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(bookSimpleDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("카테고리 ID로 도서 조회")
    void testGetBooksByCategoryId() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findBooksByCategoryId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        Page<BookSimpleResponseDto> result = bookService.getBooksByCategoryId(pageable, 1L);

        assertEquals(1, result.getTotalElements());
        assertEquals(bookSimpleDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("기여자 ID로 도서 조회")
    void testGetBooksByContributorId() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findBooksByContributorId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        Page<BookSimpleResponseDto> result = bookService.getBooksByContributorId(pageable, 1L);

        assertEquals(1, result.getTotalElements());
        assertEquals(bookSimpleDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("도서 ID로 특정 도서 조회 - 성공")
    void testGetBookByIdSuccess() {
        when(bookRepository.findBookByBookId(anyLong())).thenReturn(Optional.of(bookResponseDto));

        BookResponseDto result = bookService.getBookById(1L);

        assertEquals(bookResponseDto, result);
    }

    @Test
    @DisplayName("도서 ID로 특정 도서 조회 - 실패")
    void testGetBookByIdFailure() {
        when(bookRepository.findBookByBookId(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }
}
