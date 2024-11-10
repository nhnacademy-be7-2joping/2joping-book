package com.nhnacademy.bookstore.coupon.repository.member.impl;

import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponQuerydslRespository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nhnacademy.bookstore.coupon.entity.QCoupon.coupon;
import static com.nhnacademy.bookstore.coupon.entity.QCouponPolicy.couponPolicy;
import static com.nhnacademy.bookstore.coupon.entity.member.QMemberCoupon.memberCoupon;

@Repository
@RequiredArgsConstructor
public class MemberCouponRepositoryImpl implements MemberCouponQuerydslRespository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberCouponResponseDto> getAllMemberCoupons(Long customerId) {
        return queryFactory.select(Projections.constructor(MemberCouponResponseDto.class,
                        memberCoupon.couponUsageId,
                        memberCoupon.coupon.id,
                        memberCoupon.receiveTime,
                        memberCoupon.invalidTime,
                        memberCoupon.isUsed,
                        memberCoupon.usedDate,
                        Projections.constructor(CouponResponseDto.class,
                                memberCoupon.coupon.id,
                                memberCoupon.coupon.name,
                                memberCoupon.coupon.createdAt,
                                memberCoupon.coupon.expiredAt,
                                memberCoupon.coupon.quantity,
                                Projections.constructor(CouponPolicyResponseDto.class,
                                        memberCoupon.coupon.couponPolicy.couponPolicyId,
                                        memberCoupon.coupon.couponPolicy.name,
                                        memberCoupon.coupon.couponPolicy.discountType.stringValue(),
                                        memberCoupon.coupon.couponPolicy.discountValue,
                                        memberCoupon.coupon.couponPolicy.usageLimit,
                                        memberCoupon.coupon.couponPolicy.duration,
                                        memberCoupon.coupon.couponPolicy.detail,
                                        memberCoupon.coupon.couponPolicy.maxDiscount)
                        )))
                .from(memberCoupon)
                .leftJoin(memberCoupon.coupon, coupon)
                .leftJoin(memberCoupon.coupon.couponPolicy, couponPolicy)
                .where(memberCoupon.member.id.eq(customerId)) // customerId로 필터링
                .fetch();  }
}
