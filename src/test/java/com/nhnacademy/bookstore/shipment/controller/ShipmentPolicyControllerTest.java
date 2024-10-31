package com.nhnacademy.bookstore.shipment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentPolicyRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.service.ShipmentPolicyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShipmentPolicyController.class)
class ShipmentPolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentPolicyService shipmentPolicyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("배송 정책 생성 테스트")
    void createShipmentPolicy() throws Exception {
        // given
        ShipmentPolicyRequestDto requestDto = new ShipmentPolicyRequestDto("정책 이름", 10000, true, 5000);
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "정책 이름", 10000, true, 5000, true, null, null);
        Mockito.when(shipmentPolicyService.createShipmentPolicy(any(ShipmentPolicyRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/bookstore/shipment-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shipmentPolicyId").value(1L))
                .andExpect(jsonPath("$.name").value("정책 이름"));
    }

    @Test
    @DisplayName("특정 배송 정책 조회 테스트")
    void getShipmentPolicy() throws Exception {
        // given
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "정책 이름", 10000, true, 5000, true, null, null);
        Mockito.when(shipmentPolicyService.getShipmentPolicy(1L)).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/bookstore/shipment-policies/1")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentPolicyId").value(1L))
                .andExpect(jsonPath("$.name").value("정책 이름"));
    }

    @Test
    @DisplayName("모든 배송 정책 조회 테스트")
    void getAllShipmentPolicies() throws Exception {
        // given
        ShipmentPolicyResponseDto responseDto1 = new ShipmentPolicyResponseDto(1L, "정책1", 10000, true, 5000, true, null, null);
        ShipmentPolicyResponseDto responseDto2 = new ShipmentPolicyResponseDto(2L, "정책2", 5000, false, 2500, true, null, null);
        Mockito.when(shipmentPolicyService.getAllShipmentPolicies()).thenReturn(List.of(responseDto1, responseDto2));

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/bookstore/shipment-policies")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shipmentPolicyId").value(1L))
                .andExpect(jsonPath("$[0].name").value("정책1"))
                .andExpect(jsonPath("$[1].shipmentPolicyId").value(2L))
                .andExpect(jsonPath("$[1].name").value("정책2"));
    }

    @Test
    @DisplayName("배송 정책 수정 테스트")
    void updateShipmentPolicy() throws Exception {
        // given
        ShipmentPolicyRequestDto requestDto = new ShipmentPolicyRequestDto("업데이트된 정책", 5000, false, 1000);
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "업데이트된 정책", 5000, false, 1000, true, null, null);
        Mockito.when(shipmentPolicyService.updateShipmentPolicy(eq(1L), any(ShipmentPolicyRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/bookstore/shipment-policies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentPolicyId").value(1L))
                .andExpect(jsonPath("$.name").value("업데이트된 정책"));
    }

    @Test
    @DisplayName("배송 정책 비활성화 테스트")
    void deactivateShipmentPolicy() throws Exception {
        // given
        Mockito.doNothing().when(shipmentPolicyService).deactivateShipmentPolicy(1L);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/bookstore/shipment-policies/1/deactivate")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }
}

