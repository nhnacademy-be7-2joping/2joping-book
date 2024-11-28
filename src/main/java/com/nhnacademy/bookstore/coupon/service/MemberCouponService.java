package com.nhnacademy.bookstore.coupon.service;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.OrderCouponResponse;
import com.nhnacademy.bookstore.user.member.entity.Member;

import java.util.List;

/**
 * MemberCouponService
 * 이 인터페이스는 회원이 보유한 쿠폰에 대한 비즈니스 로직을 정의합니다.
 * 특정 회원의 모든 쿠폰을 조회하는 기능을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface MemberCouponService {
    List<MemberCouponResponseDto> getAllMemberCoupons(long customerId);
    List<MemberCouponResponseDto> getAllMemberUsedCoupons(long customerId);
    List<OrderCouponResponse> getAllMemberOrderCoupons(long customerId);
    boolean saveWelcomeCoupon(Member member);

}
