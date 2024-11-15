package com.nhnacademy.bookstore.bookset.category.dto.response;

import com.nhnacademy.bookstore.bookset.category.entity.Category;

public record GetParentCategoryResponse(

        Long categoryId,
        String name,
        Long parentCategoryId

) {

    public static GetParentCategoryResponse from(Category category) {
        return new GetParentCategoryResponse(
                category.getCategoryId(),
                category.getName(),
                category.getParentCategory().getCategoryId()
                );
    }
}
