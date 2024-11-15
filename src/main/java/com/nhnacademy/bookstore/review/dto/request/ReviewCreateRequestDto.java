package com.nhnacademy.bookstore.review.dto.request;

import jakarta.validation.constraints.Positive;


/**
 * 리뷰 생성할 때 Request DTO
 *
 * @author : 이유현
 * @date : 2024-11-12
 */

public record ReviewCreateRequestDto (
        @Positive Long reviewId,
        @Positive Long orderDetailId,
        @Positive Long customerId,
        @Positive Long bookId,
        int ratingValue,
        String title,
        String text,
        String image // TODO 임시
)
{}
