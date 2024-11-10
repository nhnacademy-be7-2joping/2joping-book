package com.nhnacademy.bookstore.coupon.mapper;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    @Mapping(source = "couponPolicy", target = "couponPolicyResponseDto")
    CouponResponseDto toCouponResponseDto(Coupon coupon);

    CouponPolicyResponseDto toResponseDto(CouponPolicy couponPolicy);
}
