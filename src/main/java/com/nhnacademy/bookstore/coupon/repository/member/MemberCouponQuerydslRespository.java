package com.nhnacademy.bookstore.coupon.repository.member;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;

import java.util.List;

public interface MemberCouponQuerydslRespository{

    List<MemberCouponResponseDto> getAllMemberCoupons(Long customerId);


}
