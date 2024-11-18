package com.nhnacademy.bookstore.bookset.category.dto.response;

import com.nhnacademy.bookstore.bookset.category.entity.Category;

/**
 * 카테고리 전체 조회 DTO
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public record GetAllCategoriesResponse(

        Long categoryId,

        String name
) {
    public static GetAllCategoriesResponse from(Category category) {
        return new GetAllCategoriesResponse(
                category.getCategoryId(),
                category.getName()
        );
    }
}
