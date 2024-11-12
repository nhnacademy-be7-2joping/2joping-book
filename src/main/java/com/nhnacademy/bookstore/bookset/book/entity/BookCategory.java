package com.nhnacademy.bookstore.bookset.book.entity;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 도서 카테고리 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_category")
public class BookCategory {
    @EmbeddedId
    private BookCategoryId id;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class BookCategoryId implements Serializable {
        private Long bookId;
        private Long categoryId;
    }
}
