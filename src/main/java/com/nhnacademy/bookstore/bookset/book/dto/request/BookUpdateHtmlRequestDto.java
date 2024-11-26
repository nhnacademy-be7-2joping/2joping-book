package com.nhnacademy.bookstore.bookset.book.dto.request;

import java.time.LocalDate;
import java.util.List;

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
        Long topCategoryId,
        Long middleCategoryId,
        Long bottomCategoryId,
        List<String> tagList,
        boolean removeThumbnailImage,
        boolean removeDetailImage
) {}
