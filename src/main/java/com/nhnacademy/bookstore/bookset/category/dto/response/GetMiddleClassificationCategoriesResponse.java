package com.nhnacademy.bookstore.bookset.category.dto.response;

import com.nhnacademy.bookstore.bookset.category.entity.Category;

public record GetMiddleClassificationCategoriesResponse(

        Long categoryId,
        Category parentCategory,
        String name

) {

    public static GetMiddleClassificationCategoriesResponse from(Category category) {
        return new GetMiddleClassificationCategoriesResponse(
                category.getCategoryId(),
                category.getParentCategory(),
                category.getName()
        );
    }
}
