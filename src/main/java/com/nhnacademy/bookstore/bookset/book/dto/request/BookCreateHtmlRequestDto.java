package com.nhnacademy.bookstore.bookset.book.dto.request;

import java.time.LocalDate;
import java.util.List;

public record BookCreateHtmlRequestDto(
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
        // List<Map<String, String>> contributorList,
        // String category,
        Long topCategoryId,        // 상위 카테고리
        Long middleCategoryId,     // 중위 카테고리
        Long bottomCategoryId,
        List<String> tagList
) {
}