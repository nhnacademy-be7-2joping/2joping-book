package com.nhnacademy.bookstore.bookset.book.dto.request;

import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 도서 생성 Request DTO
 *
 * @author : 양준하
 * @date : 2024-10-23
 */

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequestDto {
    private Publisher publisher;
    private String title;
    private String description;
    private LocalDate publishedDate;
    private String isbn;
    private int retailPrice;
    private int sellingPrice;
    private boolean giftWrappable;
    private boolean isActive;
    private int remainQuantity;
    private int views;
    private int likes;
//    private List<ContributorResponseDto> contributorList;
//    private List<CategoryResponseDto> categoryList;
//    private List<TagResponseDto> tagList;
    private String thumbnail;
}