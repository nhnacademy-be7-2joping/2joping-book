package com.nhnacademy.bookstore.refund.controller;

import com.nhnacademy.bookstore.refund.dto.response.RefundResponseDto;
import com.nhnacademy.bookstore.refund.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/refund")
public class RefundController {

    private final RefundService refundService;

    @GetMapping
    public ResponseEntity<List<RefundResponseDto>> getMemberRefundHistory(
            @RequestHeader("X-Customer-Id") String customerId) {

        List<RefundResponseDto> responseDto = refundService.getRefunds(Long.parseLong(customerId));

        return ResponseEntity.ok(responseDto);

    }

}
