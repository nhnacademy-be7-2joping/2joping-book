package com.nhnacademy.bookstore.bookset.category.dto.response;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import jakarta.annotation.Nullable;

/**
 * 카테고리 단일 조회 DTO
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public record GetCategoryResponse(

        Long categoryId,

        String name,

        @Nullable
        Long parentCategoryId
) {
        public static GetCategoryResponse from(Category category) {
                return new GetCategoryResponse(
                        category.getCategoryId(),
                        category.getName(),
                        category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null
                );
        }
}
