package com.nhnacademy.bookstore.bookset.category.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

/**
 * 카테고리 생성 DTO
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public record CategoryRequestDto(

        @Nullable
        Long parentCategoryId,

        @NotBlank(message = "카테고리 이름은 필수입니다.")
        String categoryName
) {
}
