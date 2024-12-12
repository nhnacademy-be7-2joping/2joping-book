package com.nhnacademy.bookstore.bookset.book.controller;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.request.BookUpdateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.*;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import com.nhnacademy.bookstore.bookset.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CategoryNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 도서 Controller
 *
 * @author : 이유현
 * @date : 2024.10.29
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/bookstore")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * 도서를 단독으로 등록하는 controller
     *
     * @return 등록한 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "도서 단독 등록", description = "새로운 도서를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "도서 생성 성공")
    @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping(value = "/admin/books/register")
    public ResponseEntity<BookCreateResponseDto> createBook(@RequestBody BookCreateRequestDto bookCreateRequestDto) {
        try {
            BookCreateResponseDto book = bookService.createBook(bookCreateRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 알라딘 API 이용해 도서 등록하는 controller
     *
     * @return 등록한 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "알라딘 API 활용 도서 등록", description = "알라딘 API를 활용해 새로운 도서를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "도서 생성 성공")
    @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping("/admin/books/register/aladin")
    public ResponseEntity<List<BookCreateAPIResponseDto>> createBooks(String query) {
        try {
            List<BookCreateAPIResponseDto> books = bookService.createBooks(query);
            return ResponseEntity.status(HttpStatus.CREATED).body(books);
        } catch (PublisherNotFoundException | ContributorNotFoundException | CategoryNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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
     * @param categoryId 조회하려는 카테고리 id
     * @return 카테고리로 조회한 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "카테고리로 도서 조회", description = "카테고리별 도서를 조회합니다.")
    @GetMapping("/books/get/category/{category-id}")// 임시로 설정해둔거
    public ResponseEntity<Page<BookSimpleResponseDto>> getBooksByCategoryId(@PathVariable("category-id") Long categoryId,
                                                                            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("카테고리별 도서를 조회하는 컨트롤러 !!");
        Page<BookSimpleResponseDto> books = bookService.getBooksByCategoryId(pageable, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * 기여자별 도서를 조회하는 controller
     *
     * @param contributorId 조회하려는 기여자 id
     * @return 기여자로 조회한 상태 코드를 담은 응답
     */
    @Operation(summary = "기여자로 도서 조회", description = "기여자별 도서를 조회합니다.")
    @GetMapping("/books/get/contributor/{contributor-id}")// 임시로 설정해둔거
    public ResponseEntity<Page<BookSimpleResponseDto>> getBooksByContributorId(@PathVariable("contributor-id") Long contributorId,
                                                                               @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BookSimpleResponseDto> books = bookService.getBooksByContributorId(pageable, contributorId);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * 특정 도서의 상세 정보를 조회하는 controller
     *
     * @param bookId 조회하려는 도서 id
     * @return 특정 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "특정 도서 상세 조회", description = "특정 도서의 상세 정보를 조회합니다.")
    @GetMapping("/books/get/book/{book-id}")
    public ResponseEntity<BookResponseDto> getBookByBookId(@PathVariable("book-id") Long bookId) {
        BookResponseDto book = bookService.getBookById(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    /**
     * 특정 도서 수정 controller
     *
     * @param bookId 조회하려는 도서 id
     * @return 특정 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "특정 도서 수정 정보 조회", description = "특정 도서 정보를 수정을 위해 기존 정보를 조회합니다.")
    @GetMapping("admin/books/{bookId}")
    public ResponseEntity<BookUpdateResponseDto> getUpdateBookByBookId(@PathVariable("bookId") Long bookId) {
        BookUpdateResponseDto book = bookService.getUpdateBookByBookId(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    /**
     * 특정 도서 수정 controller
     *
     * @param bookId 조회하려는 도서 id
     * @return 특정 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "특정 도서 수정", description = "특정 도서 정보를 수정합니다.")
    @PutMapping("admin/books/{bookId}")
    public ResponseEntity<BookUpdateResultResponseDto> updateBook(@PathVariable("bookId") Long bookId, @RequestBody BookUpdateRequestDto bookUpdateRequestDto) {
        BookUpdateResultResponseDto book = bookService.updateBook(bookId, bookUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    /**
     * 특정 도서를 삭제하는 controller
     *
     * @param bookId 삭제하려는 도서 id
     * @return 삭제된 도서 ID와 상태 코드를 담은 응답
     */
    @Operation(summary = "특정 도서 삭제", description = "특정 도서를 삭제합니다.")
    @PutMapping("/admin/books/{book-id}/deactivate")
    public ResponseEntity<Long> deactivateBook(@PathVariable("book-id") Long bookId) {
        bookService.deactivateBook(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(bookId);
    }
}
