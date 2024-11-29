package com.nhnacademy.bookstore.point.dto.request;

public record PointUseRequest(

        Long customerId,
        Integer pointAmount
) {
}
