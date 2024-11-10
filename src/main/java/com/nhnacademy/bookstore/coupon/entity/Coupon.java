package com.nhnacademy.bookstore.coupon.entity;


import com.nhnacademy.bookstore.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Coupon 엔티티 클래스
 * 쿠폰 정보와 쿠폰 정책을 관리하는 클래스입니다.
 * 특정 정책을 기반으로 생성된 쿠폰의 정보(생성일, 만료일, 수량 등)를 포함합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(length = 30, unique = true)
    private String name;

    private LocalDate createdAt;

    private LocalDate expiredAt;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "coupon_policy_id", referencedColumnName = "coupon_policy_id")
    @Setter
    private CouponPolicy couponPolicy;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberCoupon> memberCoupons;

    public static Coupon from(CouponRequestDto dto) {
        Coupon coupon = new Coupon();
        coupon.name = dto.name();
        coupon.createdAt = LocalDate.now();
        coupon.expiredAt = dto.expiredAt();
        coupon.quantity = dto.quantity();
        return coupon;
    }
}