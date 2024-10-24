package com.nhnacademy.bookstore.bookset.book.dto.request;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import lombok.*;

/**
 * 도서 기여자 Request DTO
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookContributorRequestDto {
    private Contributor contributor;
    private ContributorRole contributorRole;
    private Book book;
}
