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
        Category parentCategory = category.getParentCategory();

        Long parentOfParentCategoryId = null;
        if (parentCategory != null) {
            parentOfParentCategoryId = parentCategory.getParentCategory() != null
                    ? parentCategory.getParentCategory().getCategoryId()
                    : null;
        }

        return new GetParentCategoryResponse(
                parentCategory != null ? parentCategory.getCategoryId() : null,
                parentCategory != null ? parentCategory.getName() : null,
                parentOfParentCategoryId
        );
    }
}
