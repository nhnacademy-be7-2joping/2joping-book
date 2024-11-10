package com.nhnacademy.bookstore.coupon.controller;


import com.nhnacademy.bookstore.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<CouponResponseDto> createCoupon(@Valid @RequestBody CouponRequestDto couponRequestDto) {
        CouponResponseDto requestDto = couponService.create(couponRequestDto);

        return ResponseEntity.ok(requestDto);
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponseDto>> getAllCoupons() {
        List<CouponResponseDto> responseDtos = couponService.getAllCouponse();

        return ResponseEntity.ok(responseDtos);
    }
}
