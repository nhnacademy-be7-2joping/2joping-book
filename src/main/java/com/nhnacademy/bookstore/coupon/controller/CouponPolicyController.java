package com.nhnacademy.bookstore.coupon.controller;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponPolicyController {

    private final CouponPolicyService couponPolicyService;

    @GetMapping("/policies")
    public ResponseEntity<List<CouponPolicyResponseDto>> getAllPolicies() {

        List<CouponPolicyResponseDto> responseDtos = couponPolicyService.getAllCouponPolicies();

        return ResponseEntity.ok(responseDtos);
    }

}
