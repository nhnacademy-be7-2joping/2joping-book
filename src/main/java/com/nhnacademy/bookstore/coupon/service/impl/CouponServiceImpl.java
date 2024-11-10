package com.nhnacademy.bookstore.coupon.service.impl;


import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.coupon.DuplicateCouponNameException;
import com.nhnacademy.bookstore.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.mapper.CouponMapper;
import com.nhnacademy.bookstore.coupon.repository.coupon.CouponRepository;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import com.nhnacademy.bookstore.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    private final CouponPolicyRepository couponPolicyRepository;

    @Override
    public CouponResponseDto create(CouponRequestDto couponRequestDto) {

        if (couponRepository.existsByName(couponRequestDto.name())) {
            throw new DuplicateCouponNameException("쿠폰 이름이 이미 존재합니다:" + couponRequestDto.name() , RedirectType.REDIRECT, "/coupons");
        }
        Optional<CouponPolicy> couponPolicy =couponPolicyRepository.findById(couponRequestDto.couponPolicyId());

        Coupon coupon = Coupon.from(couponRequestDto);
        coupon.setCouponPolicy(couponPolicy.get());

        CouponResponseDto responseDto = couponMapper.toCouponResponseDto(couponRepository.save(coupon));

        return responseDto;

    }

    @Override
    public List<CouponResponseDto> getAllCouponse() {

        List<CouponResponseDto> responseDtos = couponRepository.findAllCoupons();

        if (responseDtos == null) {
            responseDtos = Collections.emptyList();
        }

        return responseDtos;
    }
}
