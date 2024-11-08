package com.nhnacademy.bookstore.bookset.book.dto.request;

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
    private String title;
    private String tableOfContent;
    private String description;
    private LocalDate publishedDate;
    private String isbn;
    private int retailPrice;
    private int sellingPrice;
    private boolean giftWrappable;
    private int remainQuantity;
    private String publisherName;
    private List<String> thumbnails;
    private String detail;
}

