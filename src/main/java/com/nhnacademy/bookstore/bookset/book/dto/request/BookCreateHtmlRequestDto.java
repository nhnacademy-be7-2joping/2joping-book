package com.nhnacademy.bookstore.bookset.book.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        String category,
        List<String> tagList
) {}