package com.nhnacademy.bookstore.coupon.controller;

import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.OrderCouponResponse;
import com.nhnacademy.bookstore.coupon.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MemberCouponController
 * 이 컨트롤러 클래스는 특정 회원의 쿠폰을 조회하기 위한 API 엔드포인트를 정의합니다.
 * 클라이언트는 이 API를 통해 특정 회원이 보유한 모든 쿠폰을 조회할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberCouponController {

    private final MemberCouponService memberCouponService;

    /**
     * 특정 회원의 모든 쿠폰 조회 엔드포인트
     * 주어진 회원 ID를 통해 해당 회원이 보유한 모든 쿠폰 목록을 조회하여 반환합니다.
     *
     * @param customerId 조회할 회원의 ID
     * @return 회원이 보유한 쿠폰 정보를 포함한 응답 DTO 목록을 포함한 ResponseEntity
     */
    @GetMapping("/coupons/order")
    public ResponseEntity<List<OrderCouponResponse>> orderCouponsByMemberId(@RequestHeader("X-Customer-Id") String customerId) {

        List<OrderCouponResponse> responseDtos = memberCouponService.getAllMemberOrderCoupons(Long.parseLong(customerId));
        return ResponseEntity.ok(responseDtos);

    }

    @GetMapping("/coupons/mypage")
    public ResponseEntity<List<MemberCouponResponseDto>> couponsByMemberId(@RequestHeader("X-Customer-Id") String customerId) {

        List<MemberCouponResponseDto> responseDtos = memberCouponService.getAllMemberCoupons(Long.parseLong(customerId));

        return ResponseEntity.ok(responseDtos);

    }

    @GetMapping("/coupons/used/mypage")
    public ResponseEntity<List<MemberCouponResponseDto>> usedCouponsByMemberId(@RequestHeader("X-Customer-Id") String customerId) {

        List<MemberCouponResponseDto> responseDtos = memberCouponService.getAllMemberUsedCoupons(Long.parseLong(customerId));

        return ResponseEntity.ok(responseDtos);

    }


}
