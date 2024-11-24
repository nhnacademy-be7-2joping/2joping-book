package com.nhnacademy.bookstore.bookset.book.dto.request;

public record ImageUrlRequestDto(
        String thumbnailImageUrl,
        String detailImageUrl
) {}