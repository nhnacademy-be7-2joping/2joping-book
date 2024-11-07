package com.nhnacademy.bookstore.admin.wrappolicy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * 포장 정책 생성 요청을 위한 DTO
 */
public record WrapPolicyRequestDto(
        @NotBlank(message = "빈 값은 들어올 수 없습니다.")
        String name,

        @Positive
        int wrapPrice,

        boolean isActive
) {}