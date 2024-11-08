package com.nhnacademy.bookstore.bookset.category.dto.response;

import com.nhnacademy.bookstore.bookset.category.entity.Category;

/**
 * 카테고리 업데이트 반환 DTO
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public record UpdateCategoryResponse(

        Long categoryId,
        Category parentCategory,
        String name
) {

    public static UpdateCategoryResponse from(Category category) {
        return new UpdateCategoryResponse(
                category.getCategoryId(),
                category.getParentCategory(),
                category.getName()
        );
    }
}
