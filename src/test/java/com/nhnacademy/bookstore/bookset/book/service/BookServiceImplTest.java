package com.nhnacademy.bookstore.bookset.book.service;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        BookSimpleResponseDto bookDto = new BookSimpleResponseDto(1L, "thumbnail1", "Book Title 1", 15000, "Publisher 1", 20000, true,List.of("Contributor 1", "Contributor 2"),List.of("Category 1", "Category 2"));
        Page<BookSimpleResponseDto> bookPage = new PageImpl<>(List.of(bookDto));
        when(bookRepository.findAllBooks(any(Pageable.class))).thenReturn(bookPage);

        // Act
        Page<BookSimpleResponseDto> result = bookService.getAllBooks(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(bookDto, result.getContent().get(0));
    }

    @Test
    void testGetBooksByCategoryId() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        BookSimpleResponseDto bookDto = new BookSimpleResponseDto(1L, "thumbnail1", "Book Title 1", 15000, "Publisher 1", 20000, true,List.of("Contributor 1", "Contributor 2"),List.of("Category 1", "Category 2"));
        Page<BookSimpleResponseDto> bookPage = new PageImpl<>(List.of(bookDto));
        when(bookRepository.findBooksByCategoryId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        // Act
        Page<BookSimpleResponseDto> result = bookService.getBooksByCategoryId(pageable, 1L);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(bookDto, result.getContent().get(0));
    }

    @Test
    void testGetBooksByContributorId() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        BookSimpleResponseDto bookDto = new BookSimpleResponseDto(1L, "thumbnail1", "Book Title 1", 15000, "Publisher 1", 20000, true,List.of("Contributor 1", "Contributor 2"),List.of("Category 1", "Category 2"));
        Page<BookSimpleResponseDto> bookPage = new PageImpl<>(List.of(bookDto));
        when(bookRepository.findBooksByContributorId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        // Act
        Page<BookSimpleResponseDto> result = bookService.getBooksByContributorId(pageable, 1L);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(bookDto, result.getContent().get(0));
    }

    @Test
    void testGetBookById() {
        // Arrange
        BookResponseDto bookResponseDto = new BookResponseDto(1L, "Publisher 1", "Book Title 1", "Description", LocalDate.of(2023, 10, 29), "1234567890123", 20000, 15000, true, true, 10, 0, 0, List.of("Contributor 1", "Contributor 2"),List.of("Category 1", "Category 2"),"thumbnail1");
        when(bookRepository.findBookByBookId(anyLong())).thenReturn(bookResponseDto);

        // Act
        BookResponseDto result = bookService.getBookById(1L);

        // Assert
        assertEquals(bookResponseDto, result);
    }
}
