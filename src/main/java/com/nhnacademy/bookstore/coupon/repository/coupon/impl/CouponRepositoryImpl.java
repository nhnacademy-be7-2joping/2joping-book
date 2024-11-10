package com.nhnacademy.bookstore.coupon.repository.coupon.impl;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.coupon.repository.coupon.CouponQuerydslRepositroy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nhnacademy.bookstore.coupon.entity.QCoupon.coupon;
import static com.nhnacademy.bookstore.coupon.entity.QCouponPolicy.couponPolicy;


@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponQuerydslRepositroy {

    private final JPAQueryFactory queryFactory;


    @Override
    public CouponResponseDto create(Coupon coupon) {
        return null;
    }

    @Override
    public List<CouponResponseDto> findAllCoupons() {
        return queryFactory.select(Projections.constructor(CouponResponseDto.class,
                        coupon.id,
                        coupon.name,
                        coupon.createdAt,
                        coupon.expiredAt,
                        coupon.quantity,
                        Projections.constructor(CouponPolicyResponseDto.class,
                                couponPolicy.couponPolicyId,
                                couponPolicy.name,
                                couponPolicy.discountType.stringValue(),
                                couponPolicy.discountValue,
                                couponPolicy.usageLimit,
                                couponPolicy.duration,
                                couponPolicy.detail,
                                couponPolicy.maxDiscount)
                ))
                .from(coupon)
                .leftJoin(coupon.couponPolicy, couponPolicy)
                .fetch();
    }
}