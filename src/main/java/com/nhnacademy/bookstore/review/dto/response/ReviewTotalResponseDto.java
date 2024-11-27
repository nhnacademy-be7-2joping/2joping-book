package com.nhnacademy.bookstore.review.dto.response;

import java.sql.Timestamp;

public record ReviewTotalResponseDto(
        Long reviewId,
        Long orderDetailId,
        Long customerId,
        Long bookId,
        int ratingValue,
        String bookName,
        String title,
        String text,
        String reviewImage,
        Timestamp createdAt,
        Timestamp updatedAt
)
{}