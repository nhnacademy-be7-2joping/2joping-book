package com.nhnacademy.bookstore.bookset.book.dto.response;

import java.time.LocalDate;
import java.util.List;

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
//        List<ContributorResponseDto> contributorList,
//        CategoryResponseDto category,
//        List<TagResponseDto> tagList,
//        List<BookContributorResponseDto> contributorList,
//        List<String> categoryList,
//        List<BookTagResponseDto> tagList,
        String contributorList,
        String categoryList,
        String tagList,
        String thumbnailImageUrl,
        String detailImageUrl
) {}