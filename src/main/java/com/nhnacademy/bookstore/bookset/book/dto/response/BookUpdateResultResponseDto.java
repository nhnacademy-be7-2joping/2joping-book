package com.nhnacademy.bookstore.bookset.book.dto.response;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;

import java.time.LocalDate;
import java.util.List;

public record BookUpdateResultResponseDto(
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
        List<ContributorResponseDto> contributorList,
        GetCategoryResponse category,
        List<TagResponseDto> tagList,
//        List<BookContributorResponseDto> contributorList,
//        List<String> categoryList,
//        List<BookTagResponseDto> tagList,
        String thumbnailImageUrl,
        String detailImageUrl
) {}