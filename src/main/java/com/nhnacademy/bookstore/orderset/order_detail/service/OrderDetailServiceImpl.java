package com.nhnacademy.bookstore.orderset.order_detail.service;

import com.nhnacademy.bookstore.common.error.exception.orderset.order_detail.OrderDetailNotFoundException;
import com.nhnacademy.bookstore.orderset.order_detail.dto.response.OrderDetailResponseDto;
import com.nhnacademy.bookstore.orderset.order_detail.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetailResponseDto> getOrderDetailByOrderId(Long orderId) {
        List<OrderDetailResponseDto> orderDetails = orderDetailRepository.findByOrderId(orderId);

        if (orderDetails.isEmpty()) {
            throw new OrderDetailNotFoundException("주문상세가 없습니다.");
        }

        return orderDetails;
    }
}

