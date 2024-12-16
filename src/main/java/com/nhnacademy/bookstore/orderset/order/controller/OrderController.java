package com.nhnacademy.bookstore.orderset.order.controller;

import com.nhnacademy.bookstore.orderset.order.dto.request.OrderPostRequest;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderRequest;
import com.nhnacademy.bookstore.orderset.order.dto.response.OrderTempResponse;
import com.nhnacademy.bookstore.orderset.order.service.OrderService;
import com.nhnacademy.bookstore.user.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;

    @PostMapping("/temp")
    public ResponseEntity<OrderTempResponse> postOrderOnRedis(
            @RequestBody @Valid OrderRequest orderRequest,
            @RequestHeader(value = "X-Customer-Id", required = false) Long customerId) {
        // redis에 주문 정보 임시등록
        OrderRequest satinizedOrderRequest = orderService.validateOrderRequest(orderRequest, customerId);
        orderService.registerOrderOnRedis(satinizedOrderRequest);
        OrderTempResponse tempResponse = new OrderTempResponse(satinizedOrderRequest.totalCost());
        return ResponseEntity.ok(tempResponse);
    }

    @PostMapping
    public ResponseEntity<?> postOrder(@RequestBody @Valid OrderPostRequest orderPostRequest,
                                       @RequestHeader(value = "X-Customer-Id", required = false) Long customerId) {
        OrderRequest orderRequest = orderService.getOrderOnRedis(orderPostRequest.orderId());

        orderService.registerOrder(orderRequest, orderPostRequest, customerId);
        return ResponseEntity.ok().build();
    }
}
