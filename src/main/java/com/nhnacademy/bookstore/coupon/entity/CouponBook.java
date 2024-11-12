package com.nhnacademy.bookstore.coupon.entity;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * CouponBook 엔티티 클래스
 * 쿠폰(Coupon)과 책(Book) 간의 다대다 관계를 나타내는 조인 테이블입니다.
 * 각 쿠폰과 책의 관계를 복합 키(CouponBookId)로 구성하여 고유성을 보장합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Entity
@Table(name = "coupon_book")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponBook {

    @EmbeddedId
    private CouponBookId id;

    @ManyToOne
    @MapsId("couponId")
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


}