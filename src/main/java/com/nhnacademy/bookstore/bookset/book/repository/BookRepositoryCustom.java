package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@NoRepositoryBean
public interface BookRepositoryCustom {
//    Page<Contributor> findContributorsByBookId(Long bookId, Pageable pageable);
    Page<BookSimpleResponseDto> findBooksByCategoryId(Pageable pageable,Long categoryId);
}
