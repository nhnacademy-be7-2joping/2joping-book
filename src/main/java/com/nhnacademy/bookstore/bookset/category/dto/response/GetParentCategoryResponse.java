package com.nhnacademy.bookstore.bookset.category.dto.response;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import jakarta.annotation.Nullable;

public record GetParentCategoryResponse(

        Long categoryId,
        String name,

        @Nullable
        Long parentCategoryId

) {
    public static GetParentCategoryResponse from(Category category) {
        Long parentCategoryId = category.getParentCategory() != null
                ? category.getParentCategory().getCategoryId()
                : null;  // 부모 카테고리가 없을 경우 null 처리

        return new GetParentCategoryResponse(
                category.getCategoryId(),
                category.getName(),
                parentCategoryId
        );
    }
}
