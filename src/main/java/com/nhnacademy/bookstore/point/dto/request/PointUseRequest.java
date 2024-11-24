package com.nhnacademy.bookstore.point.dto.request;

public record PointUseRequest(
        Long orderId,
        Long customerId,
        Integer pointAmount
) {

}
