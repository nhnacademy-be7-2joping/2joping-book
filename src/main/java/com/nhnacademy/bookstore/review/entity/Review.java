package com.nhnacademy.bookstore.review.entity;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.OrderDetail;
import com.nhnacademy.bookstore.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Member member;

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

    public Review(OrderDetail orderDetail, Member member, Book book, String title, String text, int ratingValue, Timestamp createdAt, Timestamp updatedAt, String imageUrl) {
        this.orderDetail = orderDetail;
        this.member = member;
        this.book = book;
        this.title = title;
        this.text = text;
        this.ratingValue = ratingValue;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imageUrl = imageUrl;
    }


    public void update(int ratingValue, String title, String text, String imageUrl, Timestamp updatedAt) {
        this.ratingValue = ratingValue;
        this.title = title;
        this.text= text;
        this.imageUrl= imageUrl;
        this.updatedAt = updatedAt;
    }

    public void clearImage() {
        this.imageUrl = null;
    }
    public void updateImage(String newUrl) {
        this.imageUrl = newUrl;
    }
}
