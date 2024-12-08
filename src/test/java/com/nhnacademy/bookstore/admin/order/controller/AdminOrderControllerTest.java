package com.nhnacademy.bookstore.admin.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.orderset.order.dto.OrderListResponseDto;
import com.nhnacademy.bookstore.orderset.order.dto.OrderStateRequestDto;
import com.nhnacademy.bookstore.orderset.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminOrderController.class)
class AdminOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 목록 조회 테스트")
    void orderList() throws Exception {
        // given
        List<OrderListResponseDto> orderList = List.of(
                new OrderListResponseDto(1L, 1, "고객1", "쿠폰1", "수령인1", "12345", "도로명주소1", "상세주소1", 1000, 2500, 500, 10000, LocalDate.now()),
                new OrderListResponseDto(2L, 2, "고객2", "쿠폰2", "수령인2", "67890", "도로명주소2", "상세주소2", 2000, 3000, 1000, 15000, LocalDate.now())
        );

        Mockito.when(orderService.getOrders()).thenReturn(orderList);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order/order-list")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].orderId").value(1L))
                .andExpect(jsonPath("$[0].customerName").value("고객1"))
                .andExpect(jsonPath("$[0].couponName").value("쿠폰1"))
                .andExpect(jsonPath("$[1].orderId").value(2L))
                .andExpect(jsonPath("$[1].receiver").value("수령인2"));
    }

    @Test
    @DisplayName("주문 상태 업데이트 테스트")
    void updateOrderState() throws Exception {
        // given
        OrderStateRequestDto requestDto = new OrderStateRequestDto(1L, 3L);

        Mockito.when(orderService.updateOrderState(1L, 3L)).thenReturn(true);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/update-state")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk());

        Mockito.when(orderService.updateOrderState(1L, 3L)).thenReturn(false);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/update-state")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isNotFound());
    }
}
