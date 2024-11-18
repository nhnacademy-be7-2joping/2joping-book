package com.nhnacademy.bookstore.review.dto.response;

import java.sql.Timestamp;

public record ReviewResponseDto(
        Long reviewId,
        Long orderDetailId,
        Long customerId,
        Long bookId,
        int ratingValue,
        String title,
        String text,
        String imageUrl, // 임시
        Timestamp createdAt,
        Timestamp updatedAt
)
{}