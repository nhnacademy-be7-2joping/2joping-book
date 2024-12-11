package com.nhnacademy.bookstore.orderset.orderdetail.controller;

import com.nhnacademy.bookstore.orderset.orderdetail.dto.response.OrderDetailResponseDto;
import com.nhnacademy.bookstore.orderset.orderdetail.service.OrderDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OrderDetailController.class)
public class OrderDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderDetailService orderDetailService;

    @Test
    @DisplayName("주문ID로 주문 상세 조회 테스트")
    void getOrderDetailsByOrderId() throws Exception {
        // Mock 데이터 설정
        List<OrderDetailResponseDto> mockOrderDetails = List.of(
                new OrderDetailResponseDto(1L, LocalDateTime.of(2023, 12, 1, 10, 0),
                        "결제 완료", "책 A", 2, 20000),
                new OrderDetailResponseDto(2L, LocalDateTime.of(2023, 12, 2, 15, 30),
                        "배송 중", "책 B", 1, 10000)
        );

        when(orderDetailService.getOrderDetailByOrderId(1L)).thenReturn(mockOrderDetails);

        // MockMvc를 사용한 API 호출
        mockMvc.perform(get("/api/v1/bookstore/od/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderDetailId").value(1L))
                .andExpect(jsonPath("$[0].orderDate").value("2023-12-01 10:00:00"))
                .andExpect(jsonPath("$[0].orderStateDescription").value("결제 완료"))
                .andExpect(jsonPath("$[0].bookTitle").value("책 A"))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].finalPrice").value(20000))
                .andExpect(jsonPath("$[1].orderDetailId").value(2L))
                .andExpect(jsonPath("$[1].orderDate").value("2023-12-02 15:30:00"))
                .andExpect(jsonPath("$[1].orderStateDescription").value("배송 중"))
                .andExpect(jsonPath("$[1].bookTitle").value("책 B"))
                .andExpect(jsonPath("$[1].quantity").value(1))
                .andExpect(jsonPath("$[1].finalPrice").value(10000));

        // 서비스 호출 검증
        verify(orderDetailService, times(1)).getOrderDetailByOrderId(1L);
    }

    @Test
    @DisplayName("고객ID로 주문 상세 조회 테스트")
    void getOrderDetailsByCustomerId() throws Exception {
        // Mock 데이터 설정
        Page<OrderDetailResponseDto> mockOrderDetails = new PageImpl<>(
                List.of(new OrderDetailResponseDto(1L, LocalDateTime.of(2023, 12, 1, 10, 0),
                        "결제 완료", "책 A", 2, 20000)),
                PageRequest.of(0, 10),
                1
        );

        when(orderDetailService.getOrderDetailByCustomerId(any(Pageable.class), eq(1L))).thenReturn(mockOrderDetails);

        // MockMvc를 사용한 API 호출
        mockMvc.perform(get("/api/v1/bookstore/od/customer")
                        .header("X-Customer-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].orderDetailId").value(1L))
                .andExpect(jsonPath("$.content[0].orderDate").value("2023-12-01 10:00:00"))
                .andExpect(jsonPath("$.content[0].orderStateDescription").value("결제 완료"))
                .andExpect(jsonPath("$.content[0].bookTitle").value("책 A"))
                .andExpect(jsonPath("$.content[0].quantity").value(2))
                .andExpect(jsonPath("$.content[0].finalPrice").value(20000));

        // 서비스 호출 검증
        verify(orderDetailService, times(1)).getOrderDetailByCustomerId(any(Pageable.class), eq(1L));
    }
}
