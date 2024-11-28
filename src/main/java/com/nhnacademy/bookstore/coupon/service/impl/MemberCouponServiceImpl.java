package com.nhnacademy.bookstore.coupon.service.impl;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.OrderCouponResponse;
import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import com.nhnacademy.bookstore.coupon.repository.coupon.CouponRepository;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponRepository;
import com.nhnacademy.bookstore.coupon.service.MemberCouponService;
import com.nhnacademy.bookstore.user.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MemberCouponServiceImpl
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
    private final CouponRepository couponRepository;

    /**
     * 특정 회원이 보유한 모든 쿠폰을 조회하여 MemberCouponResponseDto 목록으로 반환합니다.
     *
     * @param customerId 조회할 회원의 ID
     * @return 회원이 보유한 쿠폰 정보를 담은 MemberCouponResponseDto 목록
     */
    @Transactional(readOnly = true)
    @Override
    public List<MemberCouponResponseDto> getAllMemberCoupons(long customerId) {

        return memberCouponRepository.getAllMemberCoupons(customerId);
    }

    @Override
    public List<MemberCouponResponseDto> getAllMemberUsedCoupons(long customerId) {

        return memberCouponRepository.getExpiredOrUsedMemberCoupons(customerId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderCouponResponse> getAllMemberOrderCoupons(long customerId) {

        return memberCouponRepository.getAllMemberOrderCoupons(customerId);
    }

    @Override
    public boolean saveWelcomeCoupon(Member member) {

        Coupon welcomeCoupon = couponRepository.findByName("WELCOME 쿠폰");

        if (welcomeCoupon == null) {
            throw new IllegalArgumentException("WELCOME 쿠폰이 존재하지 않습니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime duration = now.plusDays(welcomeCoupon.getCouponPolicy().getDuration());
        MemberCoupon memberCoupon = MemberCoupon.saveMemberCoupon(welcomeCoupon, member, now, duration);

        memberCouponRepository.save(memberCoupon);

        return true;
    }


}
