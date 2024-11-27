package com.nhnacademy.bookstore.admin.wrap.dto.request;

public record WrapRequestDto (
        WrapDetailRequestDto wrapDetailRequestDto,
        WrapImageUrlRequestDto imageUrlRequestDto
) {}