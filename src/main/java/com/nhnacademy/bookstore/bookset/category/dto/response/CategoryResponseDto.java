package com.nhnacademy.bookstore.bookset.category.dto.response;

/**
 * 카테고리 업데이트 반환 DTO
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public record CategoryResponseDto(
        Long categoryId,
        String name,
        Long parentCategoryId
) {}
