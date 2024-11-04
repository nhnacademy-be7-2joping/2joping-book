package com.nhnacademy.bookstore.bookset.category.dto.response;

import com.nhnacademy.bookstore.bookset.category.entity.Category;

public record UpdateCategoryResponse(

        Long categoryId,
        Category subcategory,
        String name
) {

    public static UpdateCategoryResponse from(Category category) {
        return new UpdateCategoryResponse(
                category.getCategoryId(),
                category.getSubcategory(),
                category.getName()
        );
    }
}
