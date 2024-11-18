package com.nhnacademy.bookstore.review.dto.request;

import jakarta.validation.constraints.Positive;


public record ReviewModifyRequestDto (
        @Positive Long reviewId,
        int ratingValue,
        String title,
        String text,
        String imageUrl  // TODO 임시
)
{}
