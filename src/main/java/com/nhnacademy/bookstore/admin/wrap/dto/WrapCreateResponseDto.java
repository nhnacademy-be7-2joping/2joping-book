package com.nhnacademy.bookstore.admin.wrap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record WrapCreateResponseDto (
        Long wrapId,

        @NotBlank
        String name,

        @Positive
        Integer wrapPrice,

        boolean isActive,

        String wrapImage
) {}