package com.nhnacademy.bookstore.orderset.orderdetail.service;

import com.nhnacademy.bookstore.orderset.orderdetail.dto.response.OrderDetailResponseDto;
import com.nhnacademy.bookstore.orderset.orderdetail.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetailResponseDto> getOrderDetailByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public Page<OrderDetailResponseDto> getOrderDetailByCustomerId(Pageable pageable, Long customerId) {
        return orderDetailRepository.findByCustomerId(pageable, customerId);
    }
}

