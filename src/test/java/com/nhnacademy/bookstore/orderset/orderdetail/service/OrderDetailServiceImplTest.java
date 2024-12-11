package com.nhnacademy.bookstore.orderset.orderdetail.service;


import com.nhnacademy.bookstore.orderset.orderdetail.dto.response.OrderDetailResponseDto;
import com.nhnacademy.bookstore.orderset.orderdetail.repository.OrderDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderDetailServiceImplTest {

    @InjectMocks
    private OrderDetailServiceImpl orderDetailService;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    private List<OrderDetailResponseDto> orderDetails;
    private Page<OrderDetailResponseDto> pagedOrderDetails;
    private Long orderId;
    private Long customerId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        orderId = 1L;
        customerId = 1L;
        pageable = PageRequest.of(0, 10);

        orderDetails = List.of(
                new OrderDetailResponseDto(1L, LocalDateTime.now(), "Completed", "Book A", 2, 20000),
                new OrderDetailResponseDto(2L, LocalDateTime.now(), "Pending", "Book B", 1, 15000)
        );

        pagedOrderDetails = new PageImpl<>(orderDetails, pageable, orderDetails.size());
    }

    @Test
    @DisplayName("주문 ID로 주문 상세 조회 - 성공")
    void getOrderDetailByOrderId() {
        // Mock 설정
        when(orderDetailRepository.findByOrderId(orderId)).thenReturn(orderDetails);

        // 실행
        List<OrderDetailResponseDto> result = orderDetailService.getOrderDetailByOrderId(orderId);

        // 검증
        assertNotNull(result);
        assertEquals(orderDetails.size(), result.size());
        assertEquals(orderDetails, result);

        // Repository 호출 검증
        verify(orderDetailRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    @DisplayName("고객 ID로 주문 상세 조회 - 성공")
    void getOrderDetailByCustomerId() {
        // Mock 설정
        when(orderDetailRepository.findByCustomerId(pageable, customerId)).thenReturn(pagedOrderDetails);

        // 실행
        Page<OrderDetailResponseDto> result = orderDetailService.getOrderDetailByCustomerId(pageable, customerId);

        // 검증
        assertNotNull(result);
        assertEquals(pagedOrderDetails.getContent().size(), result.getContent().size());
        assertEquals(pagedOrderDetails, result);

        // Repository 호출 검증
        verify(orderDetailRepository, times(1)).findByCustomerId(pageable, customerId);
    }
}
