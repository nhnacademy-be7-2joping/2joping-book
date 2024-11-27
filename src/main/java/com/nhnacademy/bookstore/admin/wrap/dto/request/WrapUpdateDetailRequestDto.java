package com.nhnacademy.bookstore.admin.wrap.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record WrapUpdateDetailRequestDto (
        @NotBlank
        String name,

        @Positive
        Integer wrapPrice,

        boolean isActive
) {}
