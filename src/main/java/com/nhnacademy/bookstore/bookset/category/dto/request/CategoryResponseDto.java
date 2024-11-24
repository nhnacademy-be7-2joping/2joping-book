package com.nhnacademy.bookstore.bookset.category.dto.request;

public record CategoryResponseDto (
        Long categoryId,
        String name,
        Long parentCategoryId
) {}