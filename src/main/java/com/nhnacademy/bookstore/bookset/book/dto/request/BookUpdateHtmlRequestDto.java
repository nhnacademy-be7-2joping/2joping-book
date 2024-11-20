package com.nhnacademy.bookstore.bookset.book.dto.request;

import java.time.LocalDate;

public record BookUpdateHtmlRequestDto(
        String title,
        String description,
        String publisherName,
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
        boolean removeThumbnailImage,
        boolean removeDetailImage
) {}
