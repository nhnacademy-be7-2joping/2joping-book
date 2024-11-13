package com.nhnacademy.bookstore.review.dto.request;

import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/**
 * 리뷰 생성할 때 Request DTO
 *
 * @author : 이유현
 * @date : 2024-11-12
 */

public record ReviewCreateRequestDto (
        @Positive Long orderDetailId,
        @Positive Long customerId,
        int ratingValue,
        String title,
        String text,
        LocalDateTime createdAt
)
{}
