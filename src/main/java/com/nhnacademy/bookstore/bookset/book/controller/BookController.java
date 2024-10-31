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

/**
 * 도서 Controller
 *
 * @author : 이유현
 * @date : 2024.10.29
 */


@RestController
@RequestMapping("/bookstore")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookCreateRequestDto bookCreateRequestDto){
        BookResponseDto book = bookService.createBook(bookCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    /**
     * 전체 도서를 조회하는 controller
     * @return 전체 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "전체 도서 조회", description = "등록된 모든 도서를 조회합니다.")
    @GetMapping("/books/read")// 임시로 설정해둔거
    public ResponseEntity<Page<BookSimpleResponseDto>> getAllBooks(
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
            Page<BookSimpleResponseDto> books = bookService.getAllBooks(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * 카테고리 도서를 조회하는 controller
     * @return 전체 도서와 상태 코드를 담은 응답
     */
    @Operation(summary = "카테고리로 도서 조회", description = "카테고리별 도서를 조회합니다.")
    @GetMapping("/books/read/{categoryId}")// 임시로 설정해둔거
    public ResponseEntity<Page<BookSimpleResponseDto>> getBooksByCategoryId(@PathVariable Long categoryId,
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BookSimpleResponseDto> books = bookService.getBooksByCategoryId(pageable, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }


}
