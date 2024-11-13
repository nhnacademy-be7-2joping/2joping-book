package com.nhnacademy.bookstore.shipment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.shipment.dto.request.CarrierRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;
import com.nhnacademy.bookstore.shipment.service.CarrierService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarrierController.class)
class CarrierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarrierService carrierService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("배송 업체 생성 테스트")
    void createCarrier() throws Exception {
        // given
        CarrierRequestDto requestDto = new CarrierRequestDto("핑핑배송", "010-1234-5678", "carrier@example.com", "https://example.com");
        CarrierResponseDto responseDto = new CarrierResponseDto(1L, "핑핑배송", "010-1234-5678", "carrier@example.com", "https://example.com");

        Mockito.when(carrierService.createCarrier(any(CarrierRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookstore/carriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carrierId").value(1L))
                .andExpect(jsonPath("$.name").value("핑핑배송"));
    }

    @Test
    @DisplayName("모든 배송 업체 조회 테스트")
    void getAllCarriers() throws Exception {
        // given
        CarrierResponseDto responseDto = new CarrierResponseDto(1L, "핑핑배송", "010-1234-5678", "carrier@example.com", "https://example.com");

        Mockito.when(carrierService.getAllCarriers()).thenReturn(Collections.singletonList(responseDto));

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/carriers")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].carrierId").value(1L))
                .andExpect(jsonPath("$[0].name").value("핑핑배송"));
    }

    @Test
    @DisplayName("특정 배송 업체 조회 테스트")
    void getCarrier() throws Exception {
        // given
        CarrierResponseDto responseDto = new CarrierResponseDto(1L, "핑핑배송", "010-1234-5678", "carrier@example.com", "https://example.com");

        Mockito.when(carrierService.getCarrier(1L)).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/carriers/1")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carrierId").value(1L))
                .andExpect(jsonPath("$.name").value("핑핑배송"));
    }

    @Test
    @DisplayName("배송 업체 수정 테스트")
    void updateCarrier() throws Exception {
        // given
        CarrierRequestDto requestDto = new CarrierRequestDto("이조핑배송", "010-9999-9999", "new@example.com", "https://newexample.com");
        CarrierResponseDto responseDto = new CarrierResponseDto(1L, "이조핑배송", "010-9999-9999", "new@example.com", "https://newexample.com");

        Mockito.when(carrierService.updateCarrier(eq(1L), any(CarrierRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/carriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carrierId").value(1L))
                .andExpect(jsonPath("$.name").value("이조핑배송"));
    }

    @Test
    @DisplayName("배송 업체 삭제 테스트")
    void deleteCarrier() throws Exception {
        // given
        Mockito.doNothing().when(carrierService).deleteCarrier(1L);

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bookstore/carriers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }
}

