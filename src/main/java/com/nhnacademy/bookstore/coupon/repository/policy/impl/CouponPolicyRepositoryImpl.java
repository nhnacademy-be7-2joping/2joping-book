package com.nhnacademy.bookstore.coupon.repository.policy.impl;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyQuerydslRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nhnacademy.bookstore.coupon.entity.QCouponPolicy.couponPolicy;



/**
 * CouponPolicyRepositoryImpl
 * 이 클래스는 Querydsl을 사용하여 활성화된 쿠폰 정책을 조회하는 커스텀 레포지토리의 구현체입니다.
 * CouponPolicy 엔티티를 조회하여 CouponPolicyResponseDto로 매핑된 활성 정책 목록을 반환합니다.
 * JPAQueryFactory를 사용하여 효율적인 데이터베이스 접근을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
@Repository
@RequiredArgsConstructor
public class CouponPolicyRepositoryImpl implements CouponPolicyQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 활성화된 쿠폰 정책을 조회하여 CouponPolicyResponseDto 목록으로 반환합니다.
     *
     * @return 활성 쿠폰 정책 정보를 담은 CouponPolicyResponseDto 목록
     */
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
                .fetch();
    }
}
