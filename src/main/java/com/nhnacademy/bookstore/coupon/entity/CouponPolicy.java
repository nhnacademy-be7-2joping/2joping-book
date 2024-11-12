package com.nhnacademy.bookstore.coupon.entity;

import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CouponPolicy 엔티티 클래스
 * 쿠폰 정책 정보를 관리하는 클래스입니다. 각 쿠폰 정책에는 할인 유형, 할인 값, 사용 한도, 유효 기간 등의
 * 세부 정보가 포함됩니다. 이 정책을 바탕으로 쿠폰이 발행되며, 정책에 따른 제약 조건도 정의됩니다.
 *
 * @author Luha
 * @since 1.0
 */
@Entity
@Table(name = "coupon_policy")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponPolicy {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "coupon_policy_id")
        private Long couponPolicyId;

        @Column(length = 20)
        private String name;

        @Enumerated(EnumType.STRING)
        private DiscountType discountType;

        private Integer discountValue;

        private Integer usageLimit;

        private Integer duration;

        private String detail;

        private Integer maxDiscount;

        private Boolean isActive;

}