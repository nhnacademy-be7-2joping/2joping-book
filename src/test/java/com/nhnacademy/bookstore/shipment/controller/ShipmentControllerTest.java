package com.nhnacademy.bookstore.shipment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;
import com.nhnacademy.bookstore.shipment.service.ShipmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentService shipmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("배송 생성 테스트")
    void createShipment() throws Exception {
        // given
        ShipmentRequestDto requestDto = new ShipmentRequestDto(1L, 1L, 1L, "빠른 배송 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");
        ShipmentResponseDto responseDto = new ShipmentResponseDto(1L, 1L, 1L, 1L, "빠른 배송 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");

        Mockito.when(shipmentService.createShipment(any(ShipmentRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookstore/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shipmentId").value(1L))
                .andExpect(jsonPath("$.trackingNumber").value("12345"));
    }

    @Test
    @DisplayName("모든 배송 조회 테스트")
    void getAllShipments() throws Exception {
        // given
        ShipmentResponseDto responseDto1 = new ShipmentResponseDto(1L, 1L, 1L, 1L, "빠른 배송 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");
        ShipmentResponseDto responseDto2 = new ShipmentResponseDto(2L, 2L, 2L, 2L, "일반 배송 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(3), "67890");

        Mockito.when(shipmentService.getAllShipments()).thenReturn(List.of(responseDto1, responseDto2));

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/shipments")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shipmentId").value(1L))
                .andExpect(jsonPath("$[0].trackingNumber").value("12345"))
                .andExpect(jsonPath("$[1].shipmentId").value(2L))
                .andExpect(jsonPath("$[1].trackingNumber").value("67890"));
    }

    @Test
    @DisplayName("배송 조회 테스트")
    void getShipment() throws Exception {
        // given
        ShipmentResponseDto responseDto = new ShipmentResponseDto(1L, 1L, 1L, 1L, "빠른 배송 요청", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");

        Mockito.when(shipmentService.getShipment(1L)).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/shipments/1")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentId").value(1L))
                .andExpect(jsonPath("$.trackingNumber").value("12345"));
    }

    @Test
    @DisplayName("배송 완료된 정보 조회 테스트")
    void getCompletedShipments() throws Exception {
        // given
        ShipmentResponseDto completedShipment = new ShipmentResponseDto(1L, 1L, 1L, 1L, "빠른 배송 요청", LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1), "12345");

        Mockito.when(shipmentService.getCompletedShipments()).thenReturn(Collections.singletonList(completedShipment));

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/shipments/completed")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shipmentId").value(1L))
                .andExpect(jsonPath("$[0].trackingNumber").value("12345"));
    }

    @Test
    @DisplayName("배송 미완료된 정보 조회 테스트")
    void getPendingShipments() throws Exception {
        // given
        ShipmentResponseDto pendingShipment = new ShipmentResponseDto(1L, 1L, 1L, 1L, "빠른 배송 요청", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "12345");

        Mockito.when(shipmentService.getPendingShipments()).thenReturn(Collections.singletonList(pendingShipment));

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/shipments/pending")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shipmentId").value(1L))
                .andExpect(jsonPath("$[0].trackingNumber").value("12345"));
    }

    @Test
    @DisplayName("배송 수정 테스트")
    void updateShipment() throws Exception {
        // given
        ShipmentRequestDto requestDto = new ShipmentRequestDto(1L, 1L, 1L, "경비실에 맞겨주세요", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");
        ShipmentResponseDto responseDto = new ShipmentResponseDto(1L, 1L, 1L, 1L, "경비실에 맞겨주세요", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "12345");

        Mockito.when(shipmentService.updateShipment(eq(1L), any(ShipmentRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/shipments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentId").value(1L))
                .andExpect(jsonPath("$.requirement").value("경비실에 맞겨주세요"));
    }

    @Test
    @DisplayName("배송 삭제 테스트")
    void deleteShipment() throws Exception {
        // given
        Mockito.doNothing().when(shipmentService).deleteShipment(1L);

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bookstore/shipments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }
}
