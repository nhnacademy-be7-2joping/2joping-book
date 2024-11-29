package com.nhnacademy.bookstore.orderset.order.controller;

import com.nhnacademy.bookstore.orderset.order.dto.request.OrderPostRequest;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderRequest;
import com.nhnacademy.bookstore.orderset.order.dto.response.OrderTempResponse;
import com.nhnacademy.bookstore.orderset.order.service.OrderService;
import com.nhnacademy.bookstore.user.customer.dto.request.CustomerRegisterRequest;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
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
    public ResponseEntity<OrderTempResponse> postOrderOnRedis(@RequestBody @Valid OrderRequest orderRequest) {
        // redis에 주문 정보 임시등록
        orderService.registerOrderOnRedis(orderRequest);
        OrderTempResponse tempResponse = new OrderTempResponse(orderRequest.totalCost());
        return ResponseEntity.ok(tempResponse);
    }

    @PostMapping
    public ResponseEntity<?> postOrder(@RequestBody @Valid OrderPostRequest orderPostRequest,
                                       @RequestHeader(value = "X-Customer-Id", required = false) Long customerId) {
        Customer customer;
        OrderRequest orderRequest = orderService.getOrderOnRedis(orderPostRequest.orderId());
        if (customerId == null) {
            // 비회원 주문인 경우
            CustomerRegisterRequest registerRequest = new CustomerRegisterRequest(
                    orderRequest.deliveryInfo().name(),
                    orderRequest.deliveryInfo().phone(),
                    orderRequest.deliveryInfo().email()
            );
            customer = customerService.saveCustomer(registerRequest);
        } else {
            customer = customerService.getCustomer(customerId);
        }

        orderService.registerOrder(orderRequest, orderPostRequest, customer);
        return ResponseEntity.ok().build();
    }
}
