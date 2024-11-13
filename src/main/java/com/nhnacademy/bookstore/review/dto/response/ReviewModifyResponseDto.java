package com.nhnacademy.bookstore.review.dto.response;

import java.time.LocalDateTime;

public record ReviewModifyResponseDto(
        Long orderDetailId,
        int ratingValue,
        String title,
        String text,
        String image,
        LocalDateTime updatedAt
)


{}
