package com.nhnacademy.bookstore.imageset.entity;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import jakarta.persistence.*;
import lombok.*;

/**
 * 도서 이미지 Entity
 *
 * @author : 이초은
 * @date : 2024-11-12
 */

@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "book_image")
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_image_id")
    private Long bookImageId;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @NonNull
    @Column(name = "image_type", nullable = false)
    private String imageType;
}