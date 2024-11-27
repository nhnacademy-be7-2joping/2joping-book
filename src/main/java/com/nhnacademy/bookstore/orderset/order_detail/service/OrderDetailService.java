package com.nhnacademy.bookstore.orderset.order_detail.service;

import com.nhnacademy.bookstore.orderset.order_detail.dto.response.OrderDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetailResponseDto> getOrderDetailByOrderId(Long OrderId);
    Page<OrderDetailResponseDto> getOrderDetailByCustomerId(Pageable pageable, Long CustomerId);
}
