package com.nhnacademy.bookstore.bookset.category.dto.request;

public record CategoryResponseDto (
        Long categoryId,
        Long parentCategoryId,
        String name
) {}