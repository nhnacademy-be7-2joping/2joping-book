package com.nhnacademy.bookstore.coupon.controller;


import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import com.nhnacademy.bookstore.coupon.dto.request.CreateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.request.UpdateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.service.CouponPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * CouponPolicyController
 * 이 컨트롤러 클래스는 쿠폰 정책을 관리하기 위한 API 엔드포인트를 정의합니다.
 * 클라이언트는 이 API를 통해 모든 쿠폰 정책 목록을 조회할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponPolicyController {

    private final CouponPolicyService couponPolicyService;

    @PostMapping("/policies")
    public ResponseEntity<Void> createConcernPost(@Valid @RequestBody CreateCouponPolicyRequest request) {
        Long couponPolicyId = couponPolicyService.createCouponPolicy(request);
        return ResponseEntity.created(URI.create("/" + couponPolicyId)).build();
    }

    @GetMapping("/policies/{coupon-policy-id}")
    public ResponseEntity<CouponPolicyResponseDto> getCouponPolicy(
            @ValidPathVariable @PathVariable("coupon-policy-id") Long couponPolicyId
    ) {
        CouponPolicyResponseDto response = couponPolicyService.getCouponPolicy(couponPolicyId);
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 쿠폰 정책 조회 엔드포인트
     * 저장된 모든 쿠폰 정책의 목록을 조회하여 반환합니다.
     *
     * @return 모든 쿠폰 정책 정보를 포함한 응답 DTO 목록을 포함한 ResponseEntity
     */
    @GetMapping("/policies/activated")
    public ResponseEntity<List<CouponPolicyResponseDto>> getAllPolicies() {

        List<CouponPolicyResponseDto> responseDtos = couponPolicyService.getAllCouponPolicies();

        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/policies/{coupon-policy-id}")
    public ResponseEntity<Long> updateCouponPolicy(
            @ValidPathVariable @PathVariable("coupon-policy-id") Long id,
            @RequestBody UpdateCouponPolicyRequest request
    ) {
        Long updatedCouponPolicyId = couponPolicyService.updateCouponPolicy(id, request);
        return ResponseEntity.ok(updatedCouponPolicyId);
    }

    @PutMapping("/policies/{coupon-policy-id}/deactivated")
    public ResponseEntity<Void> deleteCouponPolicy(@ValidPathVariable @PathVariable("coupon-policy-id") Long id) {
        couponPolicyService.deleteCouponPolicy(id);
        return ResponseEntity.noContent().build();
    }
}
