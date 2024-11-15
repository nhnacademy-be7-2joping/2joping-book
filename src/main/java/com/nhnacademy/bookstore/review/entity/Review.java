package com.nhnacademy.bookstore.review.entity;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * 리뷰 Entity
 *
 * @author : 이유현
 * @date : 2024-11-12
 */

@Entity
@Table(name = "review")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Review {

    @EmbeddedId
    private ReviewId reviewId;

    @MapsId("orderDetailId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "rating_value", columnDefinition = "TINYINT")
    private int ratingValue;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "image_url")
    private String imageUrl;


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class ReviewId implements Serializable {
        private Long orderDetailId;
    }

    public Review(OrderDetail orderDetail, Customer customer, Book book, String title, String text, int ratingValue, String imageUrl) {
        this.orderDetail = orderDetail;
        this.customer = customer;
        this.book = book;
        this.title = title;
        this.text = text;
        this.ratingValue = ratingValue;
        this.imageUrl = imageUrl;
        this.createdAt= Timestamp.valueOf(LocalDateTime.now());
    }

}
