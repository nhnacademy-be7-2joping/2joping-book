package com.nhnacademy.bookstore.bookset.book.controller;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 도서 Controller
 *
 * @author : 이유현
 * @date : 2024.10.29
 */


@RestController
@RequestMapping("/api/v1/bookstore")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;



    /**
     * 전체 도서를 조회하는 controller
     *
     * @return 전체 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "전체 도서 조회", description = "등록된 모든 도서를 조회합니다.")
    @GetMapping("/books/get")// 임시로 설정해둔거
    public ResponseEntity<Page<BookSimpleResponseDto>> getAllBooks(
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BookSimpleResponseDto> books = bookService.getAllBooks(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * 카테고리별 도서를 조회하는 controller
     *
     * @return 카테고리로 조회한 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "카테고리로 도서 조회", description = "카테고리별 도서를 조회합니다.")
    @GetMapping("/books/get/category/{categoryId}")// 임시로 설정해둔거
    public ResponseEntity<Page<BookSimpleResponseDto>> getBooksByCategoryId(@PathVariable Long categoryId,
                                                                            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BookSimpleResponseDto> books = bookService.getBooksByCategoryId(pageable,categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * 기여자별 도서를 조회하는 controller
     *
     * @return 기여자로 조회한 상태 코드를 담은 응답
     */
    @Operation(summary = "기여자로 도서 조회", description = "기여자별 도서를 조회합니다.")
    @GetMapping("/books/get/contributor/{contributorId}")// 임시로 설정해둔거
    public ResponseEntity<Page<BookSimpleResponseDto>> getBooksByContributorId(@PathVariable Long contributorId,
                                                                               @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BookSimpleResponseDto> books = bookService.getBooksByContributorId(pageable, contributorId);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * 특정 도서의 상세 정보를 조회하는 controller
     *
     * @return 특정 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "특정 도서 상세 조회", description = "특정 도서의 상세 정보를 조회합니다.")
    @GetMapping("/books/get/book/{bookId}")
    public ResponseEntity<BookResponseDto> getBookByBookId(@PathVariable Long bookId) {
        BookResponseDto book = bookService.getBookById(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }
}



