package com.nhnacademy.bookstore.coupon.controller;

import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * MemberCouponController
 *
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
     *
     * 주어진 회원 ID를 통해 해당 회원이 보유한 모든 쿠폰 목록을 조회하여 반환합니다.
     *
     * @param customerId 조회할 회원의 ID
     * @return 회원이 보유한 쿠폰 정보를 포함한 응답 DTO 목록을 포함한 ResponseEntity
     */
    @GetMapping("/coupons/{member-id}")
    public ResponseEntity<List<MemberCouponResponseDto>> getCouponsByMemberId(@ValidPathVariable @PathVariable("member-id") Long customerId) {

        List<MemberCouponResponseDto> responseDtos = memberCouponService.getAllMemberCoupons(customerId);

        return ResponseEntity.ok(responseDtos);

    }
}
