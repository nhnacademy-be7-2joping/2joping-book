package com.nhnacademy.bookstore.admin.wrap.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * 포장 정책 생성 요청을 위한 DTO
 */
public record WrapModifyRequestDto(
        @NotBlank(message = "포장상품의 이름은 빈 값이 될 수 없습니다.")
        String name,

        @Positive
        int wrapPrice,

        boolean isActive
) {}