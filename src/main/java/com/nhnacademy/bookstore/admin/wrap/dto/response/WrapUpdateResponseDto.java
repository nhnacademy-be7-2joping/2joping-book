package com.nhnacademy.bookstore.admin.wrap.dto.response;

public record WrapUpdateResponseDto (
        Long wrapId,
        String name,
        int wrapPrice,
        boolean isActive,
        String wrapImage
) {}