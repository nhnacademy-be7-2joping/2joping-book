package com.nhnacademy.bookstore.bookset.category.dto.response;

import jakarta.annotation.Nullable;

public record GetCategoryResponse(

        Long categoryId,

        String name,

        @Nullable
        Long parentCategoryId
) {
}
