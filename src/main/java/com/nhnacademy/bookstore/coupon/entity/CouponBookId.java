package com.nhnacademy.bookstore.coupon.entity;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * CouponBookId 클래스
 * CouponBook 엔티티에서 사용되는 복합 키를 정의하는 클래스입니다.
 * 쿠폰(Coupon)과 책(Book)의 ID를 복합 키로 묶어 고유성을 보장하며, @Embeddable을 통해
 * JPA 엔티티의 기본 키로 사용될 수 있도록 합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CouponBookId implements Serializable {

    private Long couponId;
    private Long bookId;

}