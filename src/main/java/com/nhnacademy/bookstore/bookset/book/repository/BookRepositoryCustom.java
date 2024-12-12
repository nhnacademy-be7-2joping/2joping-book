package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookUpdateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Map;
import java.util.Optional;

@NoRepositoryBean
public interface BookRepositoryCustom {

    Page<BookSimpleResponseDto> findAllBooks(Pageable pageable);

    Page<BookSimpleResponseDto> findBooksByContributorId(Pageable pageable, Long contributorId);

    Page<BookSimpleResponseDto> findBooksByCategoryId(Pageable pageable, Long categoryId);

    Optional<BookResponseDto> findBookByBookId(Long bookId);

    Map<String, Long> getCategoryHierarchy(Long lowestCategoryId);

    Optional<BookUpdateResponseDto> findUpdateBookByBookId(Long bookId);
}