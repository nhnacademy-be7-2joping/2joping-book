package com.nhnacademy.bookstore.coupon.repository.member.impl;

import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.OrderCouponResponse;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponQuerydslRespository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.nhnacademy.bookstore.coupon.entity.QCoupon.coupon;
import static com.nhnacademy.bookstore.coupon.entity.QCouponPolicy.couponPolicy;
import static com.nhnacademy.bookstore.coupon.entity.member.QMemberCoupon.memberCoupon;

/**
 * MemberCouponRepositoryImpl
 * 이 클래스는 Querydsl을 사용하여 특정 회원의 쿠폰 정보를 조회하는 커스텀 레포지토리 구현체입니다.
 * 회원 ID를 기반으로 해당 회원이 보유한 쿠폰 목록을 조회하며, 쿠폰 및 쿠폰 정책에 대한 상세 정보도 함께 제공합니다.
 * JPAQueryFactory를 사용하여 효율적으로 데이터베이스 접근을 수행합니다.
 *
 * @since 1.0
 * @author Luha
 */
@Repository
@RequiredArgsConstructor
public class MemberCouponRepositoryImpl implements MemberCouponQuerydslRespository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberCouponResponseDto> getExpiredOrUsedMemberCoupons(Long customerId) {
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
                .where(
                        memberCoupon.member.id.eq(customerId) // 특정 회원 필터링
                                .and(memberCoupon.isUsed.eq(true) // 사용된 쿠폰
                                        .or(memberCoupon.invalidTime.before(LocalDateTime.now()))) // 기간 만료된 쿠폰
                )
                .fetch();
    }

    /**
     * 특정 회원의 모든 쿠폰을 조회하여 MemberCouponResponseDto 목록으로 반환합니다.
     * 쿠폰과 쿠폰 정책 정보가 포함된 상세 정보를 조회합니다.
     *
     * @param customerId 조회할 회원의 ID
     * @return 해당 회원이 보유한 모든 쿠폰 정보를 담은 MemberCouponResponseDto 목록
     */
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
                .where(
                        memberCoupon.member.id.eq(customerId), // customerId로 필터링
                        memberCoupon.invalidTime.isNull().or(memberCoupon.invalidTime.after(LocalDateTime.now())), // 유효하지 않은 시간 제외
                        memberCoupon.isUsed.eq(false) // 사용되지 않은 쿠폰만
                )
                .fetch();
    }


    @Override
    public List<OrderCouponResponse> getAllMemberOrderCoupons(Long customerId) {
        return queryFactory.select(Projections.constructor(OrderCouponResponse.class,
                        memberCoupon.couponUsageId,
                        memberCoupon.coupon.name,
                        memberCoupon.invalidTime,
                        memberCoupon.coupon.couponPolicy.discountType,
                        memberCoupon.coupon.couponPolicy.discountValue,
                        memberCoupon.coupon.couponPolicy.usageLimit,
                        memberCoupon.coupon.couponPolicy.detail,
                        memberCoupon.coupon.couponPolicy.maxDiscount
                ))
                .from(memberCoupon)
                .leftJoin(memberCoupon.coupon, coupon)
                .leftJoin(memberCoupon.coupon.couponPolicy, couponPolicy)
                .where(
                        memberCoupon.member.id.eq(customerId), // customerId로 필터링
                        memberCoupon.invalidTime.isNull().or(memberCoupon.invalidTime.after(LocalDateTime.now())), // 유효하지 않은 시간 제외
                        memberCoupon.isUsed.eq(false) // 사용되지 않은 쿠폰만
                )
                .fetch();
    }

}
