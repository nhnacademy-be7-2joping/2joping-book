package com.nhnacademy.bookstore.review.dto.request;

import java.time.LocalDateTime;

public record ReviewModifyRequestDto (
        int ratingValue,
        String title,
        String text,
        LocalDateTime updatedAt
)
{}
