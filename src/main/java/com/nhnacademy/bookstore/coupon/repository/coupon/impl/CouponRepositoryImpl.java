package com.nhnacademy.bookstore.coupon.repository.coupon.impl;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.repository.coupon.CouponQuerydslRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nhnacademy.bookstore.coupon.entity.QCoupon.coupon;
import static com.nhnacademy.bookstore.coupon.entity.QCouponPolicy.couponPolicy;

/**
 * CouponRepositoryImpl
 * 이 클래스는 Querydsl을 사용하여 쿠폰 데이터를 조회하는 커스텀 레포지토리의 구현체입니다.
 * Coupon과 CouponPolicy 엔티티를 조인하여 CouponResponseDto와 CouponPolicyResponseDto로 매핑된 결과를 반환합니다.
 * JPAQueryFactory를 사용하여 효율적인 데이터베이스 접근을 제공합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 모든 쿠폰을 조회하는 메서드로, 쿠폰과 연관된 쿠폰 정책을 함께 반환합니다.
     * Coupon과 CouponPolicy 엔티티를 조인하여 CouponResponseDto 목록으로 반환합니다.
     *
     * @return 모든 쿠폰 정보가 담긴 CouponResponseDto 목록
     */
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