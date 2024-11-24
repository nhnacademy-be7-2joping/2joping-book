package com.nhnacademy.bookstore.coupon.repository.member;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.OrderCouponResponse;

import java.util.List;

/**
 * MemberCouponQuerydslRepository
 * 이 인터페이스는 Querydsl을 사용하여 특정 회원의 쿠폰 정보를 조회하기 위한 커스텀 레포지토리입니다.
 * 회원 ID를 기반으로 해당 회원이 보유한 모든 쿠폰을 조회하는 메서드를 정의합니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface MemberCouponQuerydslRespository{
    List<MemberCouponResponseDto> getExpiredOrUsedMemberCoupons(Long customerId);
    List<MemberCouponResponseDto> getAllMemberCoupons(Long customerId);
    List<OrderCouponResponse> getAllMemberOrderCoupons(Long customerId);


}
