package com.nhnacademy.bookstore.bookset.book.dto.response;

import jakarta.validation.constraints.Positive;

public record BookTagResponseDto (
        @Positive Long tagId,
        String tagName
) {}
