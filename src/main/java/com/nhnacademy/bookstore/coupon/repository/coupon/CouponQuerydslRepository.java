package com.nhnacademy.bookstore.coupon.repository.coupon;


import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;

import java.util.List;


/**
 * CouponQuerydslRepository
 * 이 인터페이스는 Querydsl을 사용하여 쿠폰 데이터를 조회하기 위한 커스텀 레포지토리입니다.
 * 모든 쿠폰 정보를 조회하는 메서드를 정의하며, 구현체에서 Querydsl을 통해 데이터베이스에 접근할 수 있습니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface CouponQuerydslRepository {

    List<CouponResponseDto> findAllCoupons();

}
