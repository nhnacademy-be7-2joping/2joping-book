package com.nhnacademy.bookstore.bookset.book.dto.response;

import java.time.LocalDate;

public record BookUpdateResponseDto (
        Long bookId,
        String publisherName,
        String title,
        String description,
        LocalDate publishedDate,
        String isbn,
        int retailPrice,
        int sellingPrice,
        boolean giftWrappable,
        boolean isActive,
        int remainQuantity,
        String contributorList,
        String categoryList,
        String tagList,
        String thumbnailImageUrl,
        String detailImageUrl
) {}