package com.nhnacademy.bookstore.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ReviewRequestDto (
        @Positive Long reviewId
) {}
