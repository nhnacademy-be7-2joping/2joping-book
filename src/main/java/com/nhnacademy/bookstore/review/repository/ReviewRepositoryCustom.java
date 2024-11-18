package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface ReviewRepositoryCustom {

        Page<ReviewResponseDto> getReviewsByBookId(Pageable pageable, @Param("bookId") Long bookId);
        Page<ReviewResponseDto> getReviewsByCustomerId(Pageable pageable,@Param("customerId") Long customerId);
}
