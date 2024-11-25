package com.nhnacademy.bookstore.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ReviewDetailRequestDto (
        @Positive Long orderDetailId,
        @Positive Long customerId,
        @Min(1) @Max(5)int ratingValue,
        String title,
        String text
)
{}