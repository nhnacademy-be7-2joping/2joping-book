package com.nhnacademy.bookstore.admin.order.controller;

import com.nhnacademy.bookstore.orderset.order.dto.OrderListResponseDto;
import com.nhnacademy.bookstore.orderset.order.dto.OrderStateRequestDto;
import com.nhnacademy.bookstore.orderset.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/order-list")
    public ResponseEntity<List<OrderListResponseDto>> orderList() {
        List<OrderListResponseDto> orderList = orderService.getOrders();
        return ResponseEntity.ok(orderList);
    }

    @PostMapping("/update-state")
    public ResponseEntity<Void> updateOrderState(@RequestBody OrderStateRequestDto orderStateRequestDto) {
        boolean result = orderService.updateOrderState(orderStateRequestDto.orderId(), orderStateRequestDto.statusId());
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
