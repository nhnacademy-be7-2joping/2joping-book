package com.nhnacademy.bookstore.orderset.order_detail.service;

import com.nhnacademy.bookstore.orderset.order_detail.dto.response.OrderDetailResponseDto;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetailResponseDto> getOrderDetailByOrderId(Long OrderId);
    List<OrderDetailResponseDto> getOrderDetailByCustomerId(Long CustomerId);
}
