//package com.nhnacademy.bookstore.bookset.book.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
//import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
//import com.nhnacademy.bookstore.bookset.book.service.BookService;
//import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(BookController.class)
//class BookControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BookService bookService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Publisher publisher;
//
//    @BeforeEach
//    void setUp() {
//        // 더미 Publisher 데이터를 초기화
//        publisher = new Publisher(1L, "Sample Publisher");
//    }
//
//    @Test
//    @DisplayName("전체 도서 조회")
//    void getAllBooks() throws Exception {
//        // Given
//        List<BookSimpleResponseDto> books = List.of(
//                new BookSimpleResponseDto(1L, "thumbnail.jpg", "Sample Book", 15000, publisher, 20000, true)
//        );
//        Page<BookSimpleResponseDto> bookPage = new PageImpl<>(books);
//        Mockito.when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);
//
//        // When
//        ResultActions result = mockMvc.perform(get("/api/v1/bookstore/books/get")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        // Then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].bookId").value(1L))
//                .andExpect(jsonPath("$.content[0].title").value("Sample Book"))
//                .andExpect(jsonPath("$.content[0].publisher.name").value("Sample Publisher"))
//                .andExpect(jsonPath("$.content[0].sellingPrice").value(15000))
//                .andExpect(jsonPath("$.content[0].retailPrice").value(20000))
//                .andExpect(jsonPath("$.content[0].isActive").value(true));
//    }
//
//    @Test
//    @DisplayName("카테고리별 도서 조회")
//    void getBooksByCategoryId() throws Exception {
//        // Given
//        List<BookSimpleResponseDto> books = List.of(
//                new BookSimpleResponseDto(1L, "thumbnail.jpg", "Sample Book", 15000, publisher, 20000, true)
//        );
//        Page<BookSimpleResponseDto> bookPage = new PageImpl<>(books);
//        Mockito.when(bookService.getBooksByCategoryId(any(Pageable.class), anyLong())).thenReturn(bookPage);
//
//        // When
//        ResultActions result = mockMvc.perform(get("/api/v1/bookstore/books/get/category/1")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        // Then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].bookId").value(1L))
//                .andExpect(jsonPath("$.content[0].title").value("Sample Book"))
//                .andExpect(jsonPath("$.content[0].publisher.name").value("Sample Publisher"))
//                .andExpect(jsonPath("$.content[0].sellingPrice").value(15000))
//                .andExpect(jsonPath("$.content[0].retailPrice").value(20000))
//                .andExpect(jsonPath("$.content[0].isActive").value(true));
//    }
//
//    @Test
//    @DisplayName("기여자별 도서 조회")
//    void getBooksByContributorId() throws Exception {
//        // Given
//        List<BookSimpleResponseDto> books = List.of(
//                new BookSimpleResponseDto(1L, "thumbnail.jpg", "Sample Book", 15000, publisher, 20000, true)
//        );
//        Page<BookSimpleResponseDto> bookPage = new PageImpl<>(books);
//        Mockito.when(bookService.getBooksByContributorId(any(Pageable.class), anyLong())).thenReturn(bookPage);
//
//        // When
//        ResultActions result = mockMvc.perform(get("/api/v1/bookstore/books/get/contributor/1")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        // Then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].bookId").value(1L))
//                .andExpect(jsonPath("$.content[0].title").value("Sample Book"))
//                .andExpect(jsonPath("$.content[0].publisher.name").value("Sample Publisher"))
//                .andExpect(jsonPath("$.content[0].sellingPrice").value(15000))
//                .andExpect(jsonPath("$.content[0].retailPrice").value(20000))
//                .andExpect(jsonPath("$.content[0].isActive").value(true));
//    }
//
//    @Test
//    @DisplayName("특정 도서 상세 조회")
//    void getBookByBookId() throws Exception {
//        // Given
//        BookResponseDto book = new BookResponseDto(
//                1L, publisher, "Detailed Book", "Book Description", LocalDate.now(), "1234567890123",
//                20000, 15000, true, true, 100, 500, 300, "thumbnail.jpg"
//        );
//        Mockito.when(bookService.getBookById(anyLong())).thenReturn(book);
//
//        // When
//        ResultActions result = mockMvc.perform(get("/api/v1/bookstore/books/get/book/1")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        // Then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.bookId").value(1L))
//                .andExpect(jsonPath("$.title").value("Detailed Book"))
//                .andExpect(jsonPath("$.publisher.name").value("Sample Publisher"))
//                .andExpect(jsonPath("$.sellingPrice").value(15000))
//                .andExpect(jsonPath("$.retailPrice").value(20000))
//                .andExpect(jsonPath("$.giftWrappable").value(true))
//                .andExpect(jsonPath("$.isActive").value(true))
//                .andExpect(jsonPath("$.remainQuantity").value(100))
//                .andExpect(jsonPath("$.views").value(500))
//                .andExpect(jsonPath("$.likes").value(300));
//    }
//}
