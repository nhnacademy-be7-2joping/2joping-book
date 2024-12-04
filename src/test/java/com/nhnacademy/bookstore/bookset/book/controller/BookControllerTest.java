package com.nhnacademy.bookstore.bookset.book.controller;

import com.nhnacademy.bookstore.bookset.book.dto.request.*;
import com.nhnacademy.bookstore.bookset.book.dto.response.*;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
    private BookUpdateRequestDto bookUpdateRequestDto;
    private BookUpdateResponseDto bookUpdateResponseDto;
    private BookUpdateResultResponseDto bookUpdateResultResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bookCreateRequestDto = mock(BookCreateRequestDto.class);
        bookCreateResponseDto = new BookCreateResponseDto(
                1L, "Book Title", "Description", "Publisher Name",
                LocalDate.of(2024, 10, 1), "1234567890123",
                20000, 15000, true, true, 10, null, null, null,
                "thumbnail-url", "detail-url"
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
                contributors, List.of("Category 1", "Category 2"), List.of(new BookTagResponseDto(1L,"Tag 1")),"thumbnail1",List.of(new ReviewResponseDto(1L,1L,1L,1L,5,"제목","내용","이미지", Timestamp.valueOf(LocalDateTime.now()),null))
        );

        BookUpdateHtmlRequestDto bookUpdateHtmlRequestDto = new BookUpdateHtmlRequestDto(
                "Updated Book Title",
                "Updated Description",
                "Updated Publisher",
                LocalDate.of(2024, 11, 1),
                "1234567890123",
                30000,
                25000,
                true,
                true,
                15,
                "Author (지은이)",
                1L,
                2L,
                3L,
                List.of("Tag1", "Tag2"),
                false,
                false
        );

        ImageUrlRequestDto imageUrlRequestDto = new ImageUrlRequestDto(
                "updated-thumbnail-url",
                "updated-detail-url"
        );

        bookUpdateRequestDto = new BookUpdateRequestDto(bookUpdateHtmlRequestDto, imageUrlRequestDto);

        bookUpdateResponseDto = new BookUpdateResponseDto(
                1L,
                "Updated Book Title",
                "Updated Description",
                "Updated Publisher",
                LocalDate.of(2024, 11, 1),
                "1234567890123",
                30000,
                25000,
                true,
                true,
                15,
                null,
                1L,
                2L,
                3L,
                null,
                "updated-thumbnail-url",
                "updated-detail-url",
                false,
                false
        );

        bookUpdateResultResponseDto = new BookUpdateResultResponseDto(
                1L,
                "Updated Publisher",
                "Updated Book Title",
                "Updated Description",
                LocalDate.of(2024, 11, 1),
                "1234567890123",
                30000,
                25000,
                true,
                true,
                15,
                List.of(),
                null,
                List.of(),
                "updated-thumbnail-url",
                "updated-detail-url"
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
    @DisplayName("도서 단독 등록 실패")
    void testCreateBookFailure() {
        when(bookService.createBook(any(BookCreateRequestDto.class))).thenThrow(new RuntimeException("Internal Error"));

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

    @Test
    @DisplayName("도서 수정 정보 조회 성공")
    void testGetUpdateBookByBookIdSuccess() {
        when(bookService.getUpdateBookByBookId(anyLong())).thenReturn(bookUpdateResponseDto);

        ResponseEntity<BookUpdateResponseDto> response = bookController.getUpdateBookByBookId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookUpdateResponseDto, response.getBody());
    }

    @Test
    @DisplayName("도서 수정 정보 조회 실패")
    void testGetUpdateBookByBookIdFailure() {
        when(bookService.getUpdateBookByBookId(anyLong())).thenThrow(new RuntimeException("도서를 찾을 수 없습니다."));

        try {
            bookController.getUpdateBookByBookId(1L);
        } catch (RuntimeException ex) {
            assertEquals("도서를 찾을 수 없습니다.", ex.getMessage());
        }
    }

    @Test
    @DisplayName("도서 수정 성공")
    void testUpdateBookSuccess() {
        when(bookService.updateBook(anyLong(), any(BookUpdateRequestDto.class)))
                .thenReturn(bookUpdateResultResponseDto);

        ResponseEntity<BookUpdateResultResponseDto> response = bookController.updateBook(1L, bookUpdateRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookUpdateResultResponseDto, response.getBody());
    }

    @Test
    @DisplayName("도서 수정 실패")
    void testUpdateBookFailure() {
        when(bookService.updateBook(anyLong(), any(BookUpdateRequestDto.class)))
                .thenThrow(new RuntimeException("도서를 찾을 수 없습니다."));

        try {
            bookController.updateBook(1L, bookUpdateRequestDto);
        } catch (RuntimeException ex) {
            assertEquals("도서를 찾을 수 없습니다.", ex.getMessage());
        }
    }

    @Test
    @DisplayName("도서 비활성화 성공")
    void testDeactivateBookSuccess() {
        Long bookId = 1L;

        doNothing().when(bookService).deactivateBook(bookId);

        ResponseEntity<Long> response = bookController.deactivateBook(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookId, response.getBody());
    }

    @Test
    @DisplayName("도서 비활성화 실패 - BookNotFoundException")
    void testDeactivateBookFailure() {
        Long bookId = 2L;

        doThrow(new RuntimeException("도서를 찾을 수 없습니다.")).when(bookService).deactivateBook(bookId);

        ResponseEntity<Long> response = null;
        try {
            response = bookController.deactivateBook(bookId);
        } catch (RuntimeException ex) {
            assertEquals("도서를 찾을 수 없습니다.", ex.getMessage());
        }

        assertEquals(null, response);
    }
}