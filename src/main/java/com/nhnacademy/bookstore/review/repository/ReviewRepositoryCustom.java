package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewTotalResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NoRepositoryBean
public interface ReviewRepositoryCustom {

        Page<ReviewResponseDto> getReviewsByBookId(Pageable pageable, @Param("bookId") Long bookId);
        Page<ReviewTotalResponseDto> getReviewsByCustomerId(Pageable pageable, @Param("customerId") Long customerId);
        Optional<ReviewResponseDto> getReviewByReviewId(Long reviewId);
}
