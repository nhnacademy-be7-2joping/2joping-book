package com.nhnacademy.bookstore.orderset.order.dto.request;

public record OrderPostRequest(
        String status,
        String orderId,
        String paymentKey,
        String method // 결제수단
) {
}
