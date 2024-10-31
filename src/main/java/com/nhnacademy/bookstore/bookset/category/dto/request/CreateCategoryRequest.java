package com.nhnacademy.bookstore.bookset.category.dto.request;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(

        @Nullable
        Category subcategoryId,

        @NotBlank(message = "카테고리 이름이 존재하지 않습니다.")
        String categoryName
) {
}
