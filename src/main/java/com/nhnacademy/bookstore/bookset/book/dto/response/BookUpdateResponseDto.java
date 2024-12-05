package com.nhnacademy.bookstore.bookset.book.dto.response;

import java.time.LocalDate;
import java.util.List;

public record BookUpdateResponseDto (
        Long bookId,
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
        List<BookContributorResponseDto> contributorList,
        Long topCategoryId,
        Long middleCategoryId,
        Long bottomCategoryId,
        List<BookTagResponseDto> tagList,
        String thumbnailImageUrl,
        String detailImageUrl,
        boolean removeThumbnailImage,
        boolean removeDetailImage
) {}