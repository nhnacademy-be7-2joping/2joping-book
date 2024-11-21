package com.nhnacademy.bookstore.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ReviewModifyDetailRequestDto(
        @Positive Long reviewId,
        @Min(1) @Max(5)int ratingValue,
        String title,
        String text
) {
}
