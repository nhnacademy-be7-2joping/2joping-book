package com.nhnacademy.bookstore.coupon.service.impl;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponRepository;
import com.nhnacademy.bookstore.coupon.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;

    @Override
    public List<MemberCouponResponseDto> getAllMemberCoupons(Long customerId) {

        List<MemberCouponResponseDto> memberCoupons = memberCouponRepository.getAllMemberCoupons(customerId);


        return memberCoupons;
    }


}
