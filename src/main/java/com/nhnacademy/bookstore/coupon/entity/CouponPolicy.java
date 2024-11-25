package com.nhnacademy.bookstore.coupon.entity;

import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

        @Builder
        private CouponPolicy(
                final String name,
                final DiscountType discountType,
                final Integer discountValue,
                final Integer usageLimit,
                final Integer duration,
                final String detail,
                final Integer maxDiscount,
                final Boolean isActive
        ) {
                this.name = name;
                this.discountType = discountType;
                this.discountValue = discountValue;
                this.usageLimit = usageLimit;
                this.duration = duration;
                this.detail = detail;
                this.maxDiscount = maxDiscount;
                this.isActive = isActive;
        }

        public void updateName(String name) {
                this.name = name;
        }

        public void updateDiscountType(DiscountType discountType) {
                this.discountType = discountType;
        }

        public void updateDiscountValue(Integer discountValue) {
                this.discountValue = discountValue;
        }

        public void updateUsageLimit(Integer usageLimit) {
                this.usageLimit = usageLimit;
        }

        public void updateDuration(Integer duration) {
                this.duration = duration;
        }

        public void updateDetail(String detail) {
                this.detail = detail;
        }

        public void updateMaxDiscount(Integer maxDiscount) {
                this.maxDiscount = maxDiscount;
        }

        public void deactivateCouponPolicy() {
                this.isActive = false;
        }

        public void updateActive(Boolean active) {
                this.isActive = active;
        }
}