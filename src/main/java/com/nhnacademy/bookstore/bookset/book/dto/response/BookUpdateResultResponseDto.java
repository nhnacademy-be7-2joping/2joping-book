package com.nhnacademy.bookstore.bookset.book.dto.response;

import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;

import java.time.LocalDate;
import java.util.List;

public record BookUpdateResultResponseDto(
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
        List<ContributorResponseDto> contributorList,
        CategoryResponseDto category,
        List<TagResponseDto> tagList,
        String thumbnailImageUrl,
        String detailImageUrl
) {}