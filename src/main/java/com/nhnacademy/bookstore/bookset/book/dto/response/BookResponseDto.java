package com.nhnacademy.bookstore.bookset.book.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 도서 Response DTO
 *
 * @author : 양준하
 * @date : 2024-10-23
 */

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {
    private String title;
    private boolean isActive;
    private String description;
    private LocalDate publishedDate;
    private String isbn;
    private int retailPrice;
    private int sellingPrice;
    private boolean giftWrappable;
    private int remainQuantity;
    private String publisherName;
    private String author;
    private int views;
    private int likes;
//    private List<ContributorResponseDto> contributorList;
//    private List<CategoryResponseDto> categoryList;
//    private List<TagResponseDto> tagList;
    private List<String> thumbnails;
    private String detail;
}
