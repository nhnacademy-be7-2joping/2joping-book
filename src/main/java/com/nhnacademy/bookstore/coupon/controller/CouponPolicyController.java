package com.nhnacademy.bookstore.coupon.controller;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CouponPolicyController
 *
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

    /**
     * 모든 쿠폰 정책 조회 엔드포인트
     * 저장된 모든 쿠폰 정책의 목록을 조회하여 반환합니다.
     *
     * @return 모든 쿠폰 정책 정보를 포함한 응답 DTO 목록을 포함한 ResponseEntity
     */
    @GetMapping("/policies")
    public ResponseEntity<List<CouponPolicyResponseDto>> getAllPolicies() {

        List<CouponPolicyResponseDto> responseDtos = couponPolicyService.getAllCouponPolicies();

        return ResponseEntity.ok(responseDtos);
    }
}
