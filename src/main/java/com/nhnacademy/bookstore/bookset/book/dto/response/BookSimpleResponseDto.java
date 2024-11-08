package com.nhnacademy.bookstore.bookset.book.dto.response;


import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import jakarta.validation.constraints.Positive;

import java.util.List;


/**
 * 도서 전체 조회할 때 간단한 Request DTO
 *
 * @author : 이유현
 * @date : 2024-10-29
 */

public record BookSimpleResponseDto (
     @Positive Long bookId,
     String thumbnail,
     String title,
     int sellingPrice,
     String publisherName,
     int retailPrice,
     boolean isActive,
     List<String> contributorList
) {}