package com.nhnacademy.bookstore.bookset.book.entity;

import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.like.entity.Like;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
    @Column(name = "book_id")
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @Column(nullable = false, length = 200)
    private String title;

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

    @Column(nullable = false, columnDefinition = "INT default 0")
    private int remainQuantity;

    @Column(nullable = false, columnDefinition = "INT default 0")
    private int views;

    @Setter
    @Column(nullable = false, columnDefinition = "INT default 0")
    private int likes;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private List<Like> likedBy; // Book과 Like의 연관관계 추가

    public void updateBook(String title, String description, Publisher publisher, LocalDate publishedDate,
                           String isbn, int retailPrice, int sellingPrice, boolean giftWrappable,
                           boolean isActive, int remainQuantity) {
        this.title = title;
        this.description = description;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
        this.retailPrice = retailPrice;
        this.sellingPrice = sellingPrice;
        this.giftWrappable = giftWrappable;
        this.isActive = isActive;
        this.remainQuantity = remainQuantity;
    }

    public void decreaseQuantity(int quantityToDecrease) {
        this.remainQuantity -= quantityToDecrease;
    }
  
    public void deactivate() {
        this.isActive = false;
    }
  
}