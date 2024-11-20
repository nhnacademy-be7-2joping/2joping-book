package com.nhnacademy.bookstore.bookset.category.dto.request;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 카테고리 생성 DTO
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public record CategoryCreateRequest(

        @Nullable
        Category parentCategory,

        @NotBlank(message = "카테고리 이름은 필수입니다.")
        String categoryName
) {
}
