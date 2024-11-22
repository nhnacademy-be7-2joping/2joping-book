package com.nhnacademy.bookstore.bookset.book.controller;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateHtmlRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.request.ImageUrlRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookContributorResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookTagResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookCreateResponseDto;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
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
    private BookCreateRequestDto bookCreateRequestDto;
    private BookCreateResponseDto bookCreateResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        BookCreateHtmlRequestDto bookHtmlDto = new BookCreateHtmlRequestDto(
                "Book Title",
                "Description",
                "Publisher Name",
                LocalDate.of(2023, 10, 29),
                "1234567890123",
                20000,
                15000,
                true,
                true,
                10,
                "김지은 (지은이)",
                "국내도서 > 경제경영",
                "재밌는, 따뜻한"
        );

        ImageUrlRequestDto imageUrlDto = new ImageUrlRequestDto(
                "thumbnail-url",
                "detail-url"
        );

        bookCreateRequestDto = new BookCreateRequestDto(bookHtmlDto, imageUrlDto);

        bookCreateResponseDto = new BookCreateResponseDto(
                1L,
                "Book Title",
                "Description",
                "Publisher Name",
                LocalDate.of(2023, 10, 29),
                "1234567890123",
                20000,
                15000,
                true,
                true,
                10,
                List.of(new ContributorResponseDto(1L, 1L, "김지은")),
                new CategoryResponseDto(1L,"경제경영", null),
                List.of(new TagResponseDto(1L, "재밌는"), new TagResponseDto(2L, "따뜻한")),
                "thumbnail-url",
                "detail-url"
        );

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
                contributors, List.of("Category 1", "Category 2"), List.of(new BookTagResponseDto(1L,"Tag 1")),"thumbnail1"
        );
    }

    @Test
    @DisplayName("도서 생성 성공")
    void testCreateBookSuccess() {
        when(bookService.createBook(any(BookCreateRequestDto.class))).thenReturn(bookCreateResponseDto);

        ResponseEntity<BookCreateResponseDto> response = bookController.createBook(bookCreateRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookCreateResponseDto, response.getBody());
    }

    @Test
    @DisplayName("도서 생성 실패 - 내부 서버 오류")
    void testCreateBookInternalServerError() {
        when(bookService.createBook(any(BookCreateRequestDto.class))).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<BookCreateResponseDto> response = bookController.createBook(bookCreateRequestDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
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