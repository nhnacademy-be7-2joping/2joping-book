package com.nhnacademy.bookstore.bookset.book.service.impl;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.common.error.exception.bookset.book.BookNotFoundException;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;


    /**
     * 전체 도서를 조회하는 메서드
     * @return 도서 객체
     */
    @Override
    public Page<BookSimpleResponseDto> getAllBooks(Pageable pageable) {
        Page<BookSimpleResponseDto> books = bookRepository.findAllBooks(pageable);
        return books;
    }

    /**
     * 카테고리로 도서를 조회하는 메서드
     * @param categoryId
     * @return 도서 객체
     */
    @Override
    public Page<BookSimpleResponseDto> getBooksByCategoryId(Pageable pageable, Long categoryId) {
        return bookRepository.findBooksByCategoryId(pageable, categoryId);
    }

    /**
     * 기여자로 도서를 조회하는 메서드
     * @param contributorId
     * @return 도서 객체
     */
    @Override
    public Page<BookSimpleResponseDto> getBooksByContributorId(Pageable pageable, Long contributorId) {
        return bookRepository.findBooksByContributorId(pageable, contributorId);
    }

    /**
     * 특정 도서를 조회하는 메서드
     * @param bookId
     * @return 도서 객체
     */
    @Override
    public BookResponseDto getBookById(Long bookId) {
        BookResponseDto book = bookRepository.findBookByBookId(bookId).orElseThrow(()-> new BookNotFoundException("도서를 찾을 수 없습니다."));
        return book;
    }

//    /**
//     * Id 리스트를 받아 도서리스트를 조회하는 메서드
//     * @param bookIds 특정 도서 아이디 리스트
//     * @return 도서목록
//     */
//    @Override
//    public List<BookResponseDto> getBooksById(List<Long> bookIds) {
//        // TODO 장바구니에서 도서 정보 조회 필요.
//        return List.of();
//    }


}
