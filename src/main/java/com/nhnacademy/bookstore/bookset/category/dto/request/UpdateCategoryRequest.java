package com.nhnacademy.bookstore.bookset.category.dto.request;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

/**
 * 카테고리 업데이트 DTO
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public record UpdateCategoryRequest(

        @Nullable
        Category parentCategory,

        @NotBlank(message = "카테고리 이름이 존재하지 않습니다.")
        String categoryName
) {
}
