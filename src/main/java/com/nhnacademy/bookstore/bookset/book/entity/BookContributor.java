package com.nhnacademy.bookstore.bookset.book.entity;

import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 도서 기여자 Entity
 *
 * @author : 양준하
 * @date : 2024-10-25
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_contributor")
public class BookContributor {
    @EmbeddedId
    private BookContributorId id;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @MapsId("contributorId")
    @JoinColumn(name = "contributor_id", nullable = false)
    private Contributor contributor;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class BookContributorId implements Serializable {
        private Long bookId;
        private Long contributorId;
    }
}