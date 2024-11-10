package com.nhnacademy.bookstore.coupon.service;

import com.nhnacademy.bookstore.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;

import java.util.List;

public interface CouponService {

    CouponResponseDto create(CouponRequestDto couponRequestDto);

    List<CouponResponseDto> getAllCouponse();
}
