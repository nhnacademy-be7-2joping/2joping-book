package com.nhnacademy.bookstore.user.tier.controller;

import com.nhnacademy.bookstore.user.tier.dto.response.MemberTierResponse;
import com.nhnacademy.bookstore.user.tier.service.TierService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TierController
 * 회원의 등급 정보를 제공하는 API 컨트롤러 클래스입니다.
 *
 * @author Luha
 * @since 1.0
 */@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/tier")
public class TierController {

    private final TierService tierService;

    /**
     * 회원 등급 정보를 반환하는 API 엔드포인트.
     *
     * @param customerId X-Customer-Id 헤더로 전달되는 고객 ID
     * @return ResponseEntity<MemberTierResponse> 회원 등급 정보가 포함된 응답
     */
    @Operation(
            summary = "회원 등급 조회",
            description = "회원의 ID를 통해 등급 정보를 조회합니다."
    )
    @GetMapping
    public ResponseEntity<MemberTierResponse> memberTier(
            @RequestHeader("X-Customer-Id") String customerId
    ){
        MemberTierResponse responseDto = tierService.getMemberTier(Long.parseLong(customerId));
        return ResponseEntity.ok(responseDto);
    }


}
