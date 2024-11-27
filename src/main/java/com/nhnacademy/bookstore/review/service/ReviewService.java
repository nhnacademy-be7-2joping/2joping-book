package com.nhnacademy.bookstore.review.service;

import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewModifyRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewTotalResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {

    //생성 C
    ReviewCreateResponseDto registerReview(ReviewCreateRequestDto reviewCreateRequestDto);

    // 조회 R
    Optional<ReviewResponseDto> getReviews(ReviewRequestDto reviewRequestDto);

    Page<ReviewResponseDto> getReviewsByBookId(Pageable pageable,Long bookId);

    Page<ReviewTotalResponseDto> getReviewsByCustomerId(Pageable pageable, Long customerId);

    // 수정 U
    ReviewModifyResponseDto modifyReview(ReviewModifyRequestDto reviewModifyRequestDto);
}
