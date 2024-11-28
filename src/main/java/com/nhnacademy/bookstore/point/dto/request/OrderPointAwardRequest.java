package com.nhnacademy.bookstore.point.dto.request;

public record OrderPointAwardRequest(

        Long customerId,
        Long orderId
) {
}
