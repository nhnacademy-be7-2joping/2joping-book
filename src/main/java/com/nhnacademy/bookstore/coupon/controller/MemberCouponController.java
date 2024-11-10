package com.nhnacademy.bookstore.coupon.controller;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberCouponController {

    private final MemberCouponService memberCouponService;

    @GetMapping("/coupons/{member-id}")
    public ResponseEntity<List<MemberCouponResponseDto>> getCouponsByMemberId(@PathVariable("member-id") Long customerId) {

        List<MemberCouponResponseDto> responseDtos = memberCouponService.getAllMemberCoupons(customerId);

        return ResponseEntity.ok(responseDtos);

    }
}
