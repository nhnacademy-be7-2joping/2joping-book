package com.nhnacademy.bookstore.bookset.category.dto.request;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import jakarta.annotation.Nullable;

public record CategoryCreateRequest(

        @Nullable
        Category parentCategory,

        String categoryName
) {
}
