package com.nhnacademy.bookstore.coupon.service.impl;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.OrderCouponResponse;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponRepository;
import com.nhnacademy.bookstore.coupon.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * MemberCouponServiceImpl
 *
 * 이 클래스는 회원이 보유한 쿠폰을 조회하는 비즈니스 로직을 구현하는 서비스 클래스입니다.
 * 특정 회원의 모든 쿠폰을 조회하는 기능을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;

    /**
     * 특정 회원이 보유한 모든 쿠폰을 조회하여 MemberCouponResponseDto 목록으로 반환합니다.
     *
     * @param customerId 조회할 회원의 ID
     * @return 회원이 보유한 쿠폰 정보를 담은 MemberCouponResponseDto 목록
     */
    @Transactional(readOnly = true)
    @Override
    public List<MemberCouponResponseDto> getAllMemberCoupons(long customerId) {

        List<MemberCouponResponseDto> memberCoupons = memberCouponRepository.getAllMemberCoupons(customerId);

        return memberCoupons;
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderCouponResponse> getAllMemberOrderCoupons(long customerId) {

        List<OrderCouponResponse> memberCoupons = memberCouponRepository.getAllMemberOrderCoupons(customerId);

        return memberCoupons;
    }


}
