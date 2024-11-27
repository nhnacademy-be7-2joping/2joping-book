package com.nhnacademy.bookstore.orderset.order_detail.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record OrderDetailResponseDto(
        Long orderDetailId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime orderDate,
        String orderStateDescription,
        String bookTitle,
        int quantity,
        int finalPrice
) {}
