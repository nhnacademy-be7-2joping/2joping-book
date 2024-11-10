package com.nhnacademy.bookstore.coupon.entity;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

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
public class CouponBookId implements Serializable {

    private Long couponId;
    private Long bookId;

    /**
     * equals 메서드
     * CouponBookId 객체 간의 동일성을 비교합니다.
     * couponId와 bookId가 동일하면 두 객체는 같은 객체로 간주됩니다.
     *
     * @param o 비교할 객체
     * @return 동일하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CouponBookId that)) return false;
        return Objects.equals(couponId, that.couponId) && Objects.equals(bookId, that.bookId);
    }

    /**
     * hashCode 메서드
     * CouponBookId 객체의 해시 코드를 생성합니다. 해시 코드는 couponId와 bookId에 기반하여 생성됩니다.
     *
     * @return 해시 코드 값
     */
    @Override
    public int hashCode() {
        return Objects.hash(couponId, bookId);
    }
}