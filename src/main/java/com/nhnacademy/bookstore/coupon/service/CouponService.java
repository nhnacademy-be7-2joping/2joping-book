package com.nhnacademy.bookstore.coupon.service;

import com.nhnacademy.bookstore.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;

import java.util.List;

/**
 * CouponService
 *
 * 이 인터페이스는 쿠폰에 대한 비즈니스 로직을 정의합니다.
 * 쿠폰 생성 및 모든 쿠폰 조회 기능을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface CouponService {

    CouponResponseDto create(CouponRequestDto couponRequestDto);

    List<CouponResponseDto> getAllCouponse();
}
