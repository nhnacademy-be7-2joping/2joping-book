package com.nhnacademy.bookstore.review.service;

import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewModifyRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    //생성 C
    ReviewCreateResponseDto registerReview(ReviewCreateRequestDto reviewCreateRequestDto);

    // 조회 R
    ReviewResponseDto getReviews(ReviewRequestDto reviewRequestDto);

    Page<ReviewResponseDto> getReviewsByBookId(Pageable pageable,Long bookId);

    // 수정 U
    ReviewModifyResponseDto modifyReview(ReviewModifyRequestDto reviewModifyRequestDto);
}
