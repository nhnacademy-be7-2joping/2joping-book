package com.nhnacademy.bookstore.orderset.order_detail.repository;

import com.nhnacademy.bookstore.orderset.order_detail.dto.response.OrderDetailResponseDto;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface OrderDetailRepositoryCustom {
    List<OrderDetailResponseDto> findByOrderId(Long orderId);
    List<OrderDetailResponseDto> findByCustomerId(Long customerId);

}
