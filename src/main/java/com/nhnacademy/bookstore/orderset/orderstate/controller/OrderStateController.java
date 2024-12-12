package com.nhnacademy.bookstore.orderset.orderstate.controller;

import com.nhnacademy.bookstore.orderset.orderstate.dto.request.CreateOrderStateRequest;
import com.nhnacademy.bookstore.orderset.orderstate.dto.request.UpdateOrderStateRequest;
import com.nhnacademy.bookstore.orderset.orderstate.dto.response.GetOrderStateResponse;
import com.nhnacademy.bookstore.orderset.orderstate.service.OrderStateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderStateController {

    private final OrderStateService orderStateService;

    @PostMapping("/")
    public ResponseEntity<Void> createOrderState(@Valid @RequestBody CreateOrderStateRequest request) {
        Long orderStateId = orderStateService.createOrderState(request);
        return ResponseEntity.created(URI.create("/" + orderStateId)).build();
    }

    @PutMapping("/{order-state-id}")
    public ResponseEntity<Void> updateOrderState(
            @PathVariable("order-state-id") Long orderStateId,
            @Valid @RequestBody UpdateOrderStateRequest request
    ) {
        orderStateService.updateOrderState(orderStateId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/{order-state-id}")
    public ResponseEntity<GetOrderStateResponse> getOrderState(@PathVariable("order-state-id") Long orderStateId) {
        GetOrderStateResponse response = orderStateService.getOrderState(orderStateId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{order-state-id}")
    public ResponseEntity<Void> deleteOrderState(@PathVariable("order-state-id") Long orderStateId) {
        orderStateService.deleteOrderState(orderStateId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
