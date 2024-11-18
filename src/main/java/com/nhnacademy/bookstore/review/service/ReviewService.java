package com.nhnacademy.bookstore.review.service;

import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewModifyRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;

public interface ReviewService {

    //생성 C
    ReviewCreateResponseDto registerReview(ReviewCreateRequestDto reviewCreateRequestDto);

    // 조회 R
    ReviewModifyResponseDto modifyReview(ReviewModifyRequestDto reviewModifyRequestDto);

    // 수정 U
}
