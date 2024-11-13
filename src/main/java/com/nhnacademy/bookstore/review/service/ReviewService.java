package com.nhnacademy.bookstore.review.service;

import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;

public interface ReviewService {

    //생성 C
    ReviewCreateResponseDto registerReview(ReviewCreateRequestDto reviewCreateRequestDto);

    // 조회 R


    // 수정 U
}
