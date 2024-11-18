package com.nhnacademy.bookstore.review.dto.response;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long orderDetailId,
        int ratingValue,
        String title,
        String text,
        String imageUrl, // 임시
        LocalDateTime createdAt
)
{}