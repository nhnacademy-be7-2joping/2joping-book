package com.nhnacademy.bookstore.bookset.category.dto.response;

public record CategoryIsActiveResponseDto(
        Long categoryId,
        String name,
        Long parentCategoryId,
        Boolean isActive
) {}