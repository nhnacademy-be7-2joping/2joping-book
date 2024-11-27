package com.nhnacademy.bookstore.orderset.order_detail.repository;

import com.nhnacademy.bookstore.orderset.order_detail.dto.response.OrderDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface OrderDetailRepositoryCustom {
    List<OrderDetailResponseDto> findByOrderId(Long orderId);
    Page<OrderDetailResponseDto> findByCustomerId(Pageable pageable, Long customerId);

}
