package com.nhnacademy.bookstore.bookset.book.entity;

import com.nhnacademy.bookstore.bookset.pulisher.Publisher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 도서 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String tableOfContent;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate publishedDate;

    @Column(nullable = false, length = 13, unique = true)
    private String isbn;

    @Column(nullable = false)
    private int retailPrice;

    @Column(nullable = false)
    private int sellingPrice;

    @Column(nullable = false)
    private boolean giftWrappable;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false, columnDefinition = "BIGINT default 0")
    private Long remainQuantity;

    @Column(nullable = false, columnDefinition = "INT default 0")
    private int views;

    @Column(nullable = false, columnDefinition = "INT default 0")
    private int likes;

}