package com.nhnacademy.bookstore.admin.wrap.dto;

public record WrapRequestDto (
        WrapDetailRequestDto wrapDetailRequestDto,
        WrapImageUrlRequestDto imageUrlRequestDto
) {}