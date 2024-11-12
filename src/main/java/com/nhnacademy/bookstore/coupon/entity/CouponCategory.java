package com.nhnacademy.bookstore.coupon.entity;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * CouponCategory 엔티티 클래스
 * 쿠폰(Coupon)과 카테고리(Category) 간의 다대다 관계를 나타내는 조인 테이블입니다.
 * 각 쿠폰과 카테고리의 관계를 복합 키(CouponCategoryId)로 구성하여 고유성을 보장합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Entity
@Table(name = "coupon_category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponCategory {

    @EmbeddedId
    private CouponCategoryId id;

    @ManyToOne
    @MapsId("couponId")
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;


}