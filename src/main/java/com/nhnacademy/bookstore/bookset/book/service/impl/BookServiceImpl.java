package com.nhnacademy.bookstore.bookset.book.service.impl;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Page<Book> books = bookRepository.findAll(pageable);

        return books.map(this::convertToBookSimpleResponseDto);
    }

    private BookSimpleResponseDto convertToBookSimpleResponseDto(Book book) {
        return new BookSimpleResponseDto(
                book.getBookId(),
                getBookThumbnail(book),
                book.getTitle(),
                book.getSellingPrice(),
                book.getPublisher().getName(),
                book.getRetailPrice(),
                book.isActive()
        );
    }

    //TODO 썸네일 이미지파일 경로 수정해야함
    private String getBookThumbnail(Book book) {
        return "temp.jpg";
    }


    /**
     * 특정 도서를 조회하는 메서드
     * @param bookId
     * @return 도서 객체
     */

    @Override
    public BookResponseDto getBookById(Long bookId) {
        Book book = bookRepository.findBookByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException("도서를 찾을 수 없습니다"));
        return convertToBookResponseDto(book);
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


    private BookResponseDto convertToBookResponseDto(Book book) {
        return new BookResponseDto(
                book.getBookId(),
                book.getPublisher().getName(),
                book.getTitle(),
                book.getDescription(),
                book.getPublishedDate(),
                book.getIsbn(),
                book.getRetailPrice(),
                book.getSellingPrice(),
                book.isGiftWrappable(),
                book.isActive(),
                book.getRemainQuantity(),
                book.getViews(),
                book.getLikes(),
                getBookThumbnail(book)
        );
    }

}
