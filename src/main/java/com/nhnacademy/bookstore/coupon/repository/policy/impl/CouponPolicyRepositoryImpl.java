package com.nhnacademy.bookstore.coupon.repository.policy.impl;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyQuerydslRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nhnacademy.bookstore.coupon.entity.QCouponPolicy.couponPolicy;


@Repository
@RequiredArgsConstructor
public class CouponPolicyRepositoryImpl implements CouponPolicyQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CouponPolicyResponseDto> findActivePolicy() {
        return queryFactory.select(Projections.constructor(CouponPolicyResponseDto.class,
                        couponPolicy.couponPolicyId,
                        couponPolicy.name,
                        couponPolicy.discountType.stringValue(),
                        couponPolicy.discountValue,
                        couponPolicy.usageLimit,
                        couponPolicy.duration,
                        couponPolicy.detail,
                        couponPolicy.maxDiscount))
                .from(couponPolicy)
                .where(couponPolicy.isActive.isTrue())
                .fetch();    }
}
