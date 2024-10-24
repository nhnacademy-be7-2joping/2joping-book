package com.nhnacademy.bookstore.bookset.book.dto.request;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import lombok.*;

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
