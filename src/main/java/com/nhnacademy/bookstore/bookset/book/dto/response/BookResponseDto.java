package com.nhnacademy.bookstore.bookset.book.dto.response;

import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * 도서 Response DTO
 *
 * @author : 이유현
 * @date : 2024-11-03
 */


public record BookResponseDto (
        @Positive Long bookId,
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
     int views,
     int likes,
//     List<ContributorResponseDto> contributorList,
//     List<CategoryResponseDto> categoryList,
//     List<TagResponseDto> tagList,
     String thumbnail
) {}