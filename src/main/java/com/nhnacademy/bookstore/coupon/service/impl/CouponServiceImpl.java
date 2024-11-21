package com.nhnacademy.bookstore.coupon.service.impl;


import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.coupon.CouponPolicyNotFoundException;
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

/**
 * CouponServiceImpl
 * 이 클래스는 쿠폰 생성 및 조회에 대한 비즈니스 로직을 구현하는 서비스 클래스입니다.
 * 쿠폰 이름 중복 검사, 쿠폰 생성 및 모든 쿠폰 조회 기능을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    private final CouponPolicyRepository couponPolicyRepository;

    /**
     * 새로운 쿠폰을 생성합니다. 쿠폰 이름이 중복될 경우 DuplicateCouponNameException이 발생합니다.
     *
     * @param couponRequestDto 생성할 쿠폰 정보가 담긴 DTO
     * @return 생성된 쿠폰 정보가 담긴 CouponResponseDto
     */
    @Override
    public CouponResponseDto create(CouponRequestDto couponRequestDto) {

        if (couponRepository.existsByName(couponRequestDto.name())) {
            throw new DuplicateCouponNameException("쿠폰 이름이 이미 존재합니다:" + couponRequestDto.name() , RedirectType.REDIRECT, "/admin/coupons");
        }
        Optional<CouponPolicy> couponPolicy =couponPolicyRepository.findById(couponRequestDto.couponPolicyId());

        Coupon coupon = Coupon.from(couponRequestDto);
        coupon.setCouponPolicy(couponPolicy.orElseThrow(() ->
                new CouponPolicyNotFoundException("존재하지 않는 쿠폰 정책입니다: " + couponRequestDto.couponPolicyId(), RedirectType.REDIRECT, "/admin/coupons")
        ));
        return couponMapper.toCouponResponseDto(couponRepository.save(coupon));

    }

    /**
     * 모든 쿠폰을 조회하여 CouponResponseDto 목록으로 반환합니다.
     * 조회 결과가 없을 경우 빈 리스트를 반환합니다.
     *
     * @return 모든 쿠폰 정보를 담은 CouponResponseDto 목록
     */
    @Override
    public List<CouponResponseDto> getAllCouponse() {

        List<CouponResponseDto> responseDtos = couponRepository.findAllCoupons();

        if (responseDtos.isEmpty()) {
            responseDtos = Collections.emptyList();
        }

        return responseDtos;
    }
}
