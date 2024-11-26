package com.nhnacademy.bookstore.orderset.order_detail.controller;

import com.nhnacademy.bookstore.orderset.order_detail.dto.response.OrderDetailResponseDto;
import com.nhnacademy.bookstore.orderset.order_detail.service.OrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bookstore/od")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @Operation(summary = "주문ID로 주문 상세 조회", description = "주문에 관련된 주문상세를 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetailResponseDto>> getOrderDetailsByOrderId(@PathVariable Long orderId) {
        List<OrderDetailResponseDto> orderDetails = orderDetailService.getOrderDetailByOrderId(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    @Operation(summary = "고객ID로 주문 상세 조회", description = "고객에 관련된 주문상세를 조회합니다.")
    @GetMapping("/customer")
    public ResponseEntity<List<OrderDetailResponseDto>> getOrderDetailsByCustomerId(@RequestHeader("X-Customer-Id") String customerId) {
        List<OrderDetailResponseDto> orderDetails = orderDetailService.getOrderDetailByCustomerId(Long.valueOf(customerId));
        return ResponseEntity.ok(orderDetails);
    }
}
