package com.nhnacademy.bookstore.orderset.orderdetail.service;

import com.nhnacademy.bookstore.orderset.orderdetail.dto.response.OrderDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetailResponseDto> getOrderDetailByOrderId(Long OrderId);
    Page<OrderDetailResponseDto> getOrderDetailByCustomerId(Pageable pageable, Long CustomerId);
}
