package com.nhnacademy.bookstore.coupon.repository.coupon;


import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.Coupon;

import java.util.List;

public interface CouponQuerydslRepositroy {

    CouponResponseDto create(Coupon coupon);

    List<CouponResponseDto> findAllCoupons();


}
