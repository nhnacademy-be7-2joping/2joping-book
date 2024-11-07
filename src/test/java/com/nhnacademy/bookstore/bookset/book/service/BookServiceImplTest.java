package com.nhnacademy.bookstore.bookset.book.service;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
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

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book sampleBook;
    private Publisher samplePublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 더미 Publisher 및 Book 데이터 생성
        samplePublisher = new Publisher(1L, "Sample Publisher");
        sampleBook = new Book(1L, samplePublisher, "Sample Title", "Sample Description", LocalDate.now(),
                "1234567890123", 20000, 15000, true, true, 100, 500, 300);
    }

    @Test
    @DisplayName("전체 도서 조회")
    void getAllBooks() {
        // Given
        List<Book> books = List.of(sampleBook);
        Page<Book> bookPage = new PageImpl<>(books);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookSimpleResponseDto> result = bookService.getAllBooks(pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(sampleBook.getBookId(), result.getContent().get(0).bookId());
        assertEquals(sampleBook.getTitle(), result.getContent().get(0).title());
    }

    @Test
    @DisplayName("특정 도서 조회 - 성공")
    void getBookById_Success() {
        // Given
        when(bookRepository.findBookByBookId(anyLong())).thenReturn(Optional.of(sampleBook));

        // When
        BookResponseDto result = bookService.getBookById(1L);

        // Then
        assertEquals(sampleBook.getBookId(), result.bookId());
        assertEquals(sampleBook.getTitle(), result.title());
        assertEquals(samplePublisher, result.publisher());
    }

    @Test
    @DisplayName("특정 도서 조회 - 실패")
    void getBookById_Fail() {
        // Given
        when(bookRepository.findBookByBookId(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    @DisplayName("카테고리로 도서 조회")
    void getBooksByCategoryId() {
        // Given
        List<BookSimpleResponseDto> books = List.of(
                new BookSimpleResponseDto(1L, "temp.jpg", "Sample Title", 15000, samplePublisher, 20000, true)
        );
        Page<BookSimpleResponseDto> bookPage = new PageImpl<>(books);
        when(bookRepository.findBooksByCategoryId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookSimpleResponseDto> result = bookService.getBooksByCategoryId(pageable, 1L);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Title", result.getContent().get(0).title());
    }

    @Test
    @DisplayName("기여자로 도서 조회")
    void getBooksByContributorId() {
        // Given
        List<BookSimpleResponseDto> books = List.of(
                new BookSimpleResponseDto(1L, "temp.jpg", "Sample Title", 15000, samplePublisher, 20000, true)
        );
        Page<BookSimpleResponseDto> bookPage = new PageImpl<>(books);
        when(bookRepository.findBooksByContributorId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookSimpleResponseDto> result = bookService.getBooksByContributorId(pageable, 1L);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Title", result.getContent().get(0).title());
    }
}

