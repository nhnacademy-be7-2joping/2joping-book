package com.nhnacademy.bookstore.bookset.category.dto.response;

import com.nhnacademy.bookstore.bookset.category.entity.Category;

public record GetParentCategoryResponse(

        Long categoryId,
        Category parentCategory,
        String name

) {

    public static GetParentCategoryResponse from(Category category) {
        return new GetParentCategoryResponse(
                category.getCategoryId(),
                category.getParentCategory(),
                category.getName()
        );
    }
}
