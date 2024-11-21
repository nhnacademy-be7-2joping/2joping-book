package com.nhnacademy.bookstore.coupon.controller;


import com.nhnacademy.bookstore.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * CouponController
 * 이 컨트롤러 클래스는 쿠폰 생성 및 조회 기능을 제공하는 API 엔드포인트를 정의합니다.
 * 클라이언트는 이 API를 통해 쿠폰을 생성하거나 모든 쿠폰 목록을 조회할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 생성 엔드포인트
     * 주어진 쿠폰 요청 데이터를 기반으로 새로운 쿠폰을 생성하고, 생성된 쿠폰 정보를 반환합니다.
     *
     * @param couponRequestDto 생성할 쿠폰의 정보가 담긴 DTO
     * @return 생성된 쿠폰의 정보가 담긴 응답 DTO를 포함한 ResponseEntity
     */
    @PostMapping("/coupons")
    public ResponseEntity<CouponResponseDto> createCoupon(@Valid @RequestBody CouponRequestDto couponRequestDto) {
        CouponResponseDto requestDto = couponService.create(couponRequestDto);

        return ResponseEntity.ok(requestDto);
    }

    /**
     * 모든 쿠폰 조회 엔드포인트
     * 저장된 모든 쿠폰의 목록을 조회하여 반환합니다.
     *
     * @return 모든 쿠폰 정보를 포함한 응답 DTO 목록을 포함한 ResponseEntity
     */
    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponseDto>> getAllCoupons() {
        List<CouponResponseDto> responseDtos = couponService.getAllCouponse();

        return ResponseEntity.ok(responseDtos);
    }
}
