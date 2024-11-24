package com.nhnacademy.bookstore.coupon.service.impl;


import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.coupon.CouponPolicyNotFoundException;
import com.nhnacademy.bookstore.coupon.dto.request.CreateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.request.UpdateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import com.nhnacademy.bookstore.coupon.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


/**
 * CouponPolicyServiceImpl
 * 이 클래스는 쿠폰 정책에 대한 비즈니스 로직을 구현하는 서비스 클래스입니다.
 * 활성화된 모든 쿠폰 정책을 조회하는 기능을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
@Service
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {

    private final CouponPolicyRepository couponPolicyRepository;

    @Override
    public Long createCouponPolicy(CreateCouponPolicyRequest request) {
        CouponPolicy couponPolicy = CouponPolicy.builder()
                .name(request.name())
                .discountType(request.discountType())
                .discountValue(request.discountValue())
                .usageLimit(request.usageLimit())
                .duration(request.duration())
                .detail(request.detail())
                .maxDiscount(request.maxDiscount())
                .isActive(request.isActive())
                .build();
        return couponPolicyRepository.save(couponPolicy).getCouponPolicyId();
    }

    @Override
    public CouponPolicyResponseDto getCouponPolicy(Long couponPolicyId) {
        CouponPolicy couponPolicy = couponPolicyRepository.findByCouponPolicyId(couponPolicyId)
                .orElseThrow(() -> new CouponPolicyNotFoundException(
                        "쿠폰 정책을 찾을 수 없습니다.",
                        RedirectType.REDIRECT,
                        "/api/v1/coupon/policies"
                ));
        return CouponPolicyResponseDto.from(couponPolicy);
    }

    /**
     * 활성화된 모든 쿠폰 정책을 조회하여 CouponPolicyResponseDto 목록으로 반환합니다.
     * 조회 결과가 없을 경우 빈 리스트를 반환합니다.
     *
     * @return 활성 쿠폰 정책 정보를 담은 CouponPolicyResponseDto 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CouponPolicyResponseDto> getAllCouponPolicies() {
        List<CouponPolicy> responseDtos = couponPolicyRepository.findByIsActiveTrue();

        if (responseDtos.isEmpty()) {
            responseDtos = Collections.emptyList();
        }

        return responseDtos.stream()
                .map(CouponPolicyResponseDto::from)
                .toList();
    }

    @Override
    public Long updateCouponPolicy(Long id, UpdateCouponPolicyRequest request) {
        CouponPolicy couponPolicy = couponPolicyRepository.findByCouponPolicyId(id)
                .orElseThrow(() -> new CouponPolicyNotFoundException(
                        "쿠폰 정책을 찾을 수 없습니다.",
                        RedirectType.REDIRECT,
                        "/api/v1/coupon/policies"
                ));

        couponPolicy.updateName(request.name());
        couponPolicy.updateDiscountType(request.discountType());
        couponPolicy.updateDiscountValue(request.discountValue());
        couponPolicy.updateUsageLimit(request.usageLimit());
        couponPolicy.updateDuration(request.duration());
        couponPolicy.updateDetail(request.detail());
        couponPolicy.updateMaxDiscount(request.maxDiscount());
        couponPolicy.updateActive(request.isActive());

        return couponPolicyRepository.save(couponPolicy).getCouponPolicyId();
    }

    @Override
    public void deleteCouponPolicy(Long id) {
        CouponPolicy couponPolicy = couponPolicyRepository.findByCouponPolicyId(id)
                .orElseThrow(() -> new CouponPolicyNotFoundException(
                        "쿠폰 정책을 찾을 수 없습니다.",
                        RedirectType.REDIRECT,
                        "/api/v1/coupon/policies"
                ));

        couponPolicy.deactivateCouponPolicy();
    }
}
