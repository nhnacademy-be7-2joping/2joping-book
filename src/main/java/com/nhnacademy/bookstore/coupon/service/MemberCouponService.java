package com.nhnacademy.bookstore.coupon.service;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;

import java.util.List;

public interface MemberCouponService {

    List<MemberCouponResponseDto> getAllMemberCoupons(Long customerId);
}
