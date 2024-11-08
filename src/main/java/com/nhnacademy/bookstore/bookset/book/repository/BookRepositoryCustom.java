package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@NoRepositoryBean
public interface BookRepositoryCustom {

    Page<BookSimpleResponseDto> findAllBooks(Pageable pageable);
    Page<BookSimpleResponseDto> findBooksByContributorId(Pageable pageable,Long contributorId);
    Page<BookSimpleResponseDto> findBooksByCategoryId(Pageable pageable,Long categoryId);
    Optional<BookResponseDto> findBookByBookId(Long bookId);
}
