package com.nhnacademy.bookstore.review.entity;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 리뷰 Entity
 *
 * @author : 이유현
 * @date : 2024-11-12
 */


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {

    @EmbeddedId
    private ReviewId reviewId;

    @MapsId("orderDetailId")
    @OneToOne
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private int ratingValue;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String imageUrl;


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class ReviewId implements Serializable {
        private Long orderDetailId;
    }
}
