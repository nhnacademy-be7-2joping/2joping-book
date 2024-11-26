package com.nhnacademy.bookstore.shipment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentPolicyRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShippingFeeResponseDto;
import com.nhnacademy.bookstore.shipment.service.ShipmentPolicyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "정책 이름", 10000, true, null, null, 5000, true);
        Mockito.when(shipmentPolicyService.createShipmentPolicy(any(ShipmentPolicyRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookstore/shipment-policies")
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
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "정책 이름", 10000, true, null, null, 5000, true);
        Mockito.when(shipmentPolicyService.getShipmentPolicy(1L)).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/shipment-policies/1")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentPolicyId").value(1L))
                .andExpect(jsonPath("$.name").value("정책 이름"));
    }

    @Test
    @DisplayName("모든 배송 정책 페이징 조회 테스트")
    void getAllShipmentPolicies() throws Exception {
        // given
        ShipmentPolicyResponseDto responseDto1 = new ShipmentPolicyResponseDto(1L, "정책1", 10000, true, null, null, 5000, true);
        ShipmentPolicyResponseDto responseDto2 = new ShipmentPolicyResponseDto(2L, "정책2", 5000, false, null, null, 2500, true);

        Page<ShipmentPolicyResponseDto> responsePage = new PageImpl<>(List.of(responseDto1, responseDto2));
        Mockito.when(shipmentPolicyService.getAllShipmentPolicies(any(Pageable.class))).thenReturn(responsePage);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/shipment-policies/pages")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].shipmentPolicyId").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("정책1"))
                .andExpect(jsonPath("$.content[1].shipmentPolicyId").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("정책2"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("활성화된 모든 배송 정책 조회 테스트")
    void getAllIsActiveShipmentPolicies() throws Exception {
        // given
        ShipmentPolicyResponseDto responseDto1 = new ShipmentPolicyResponseDto(1L, "정책1", 10000, true, null, null, 5000, true);
        ShipmentPolicyResponseDto responseDto2 = new ShipmentPolicyResponseDto(2L, "정책2", 5000, false, null, null, 2500, true);
        Mockito.when(shipmentPolicyService.getAllIsActiveShipmentPolicies()).thenReturn(List.of(responseDto1, responseDto2));

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/shipment-policies")
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
        ShipmentPolicyResponseDto responseDto = new ShipmentPolicyResponseDto(1L, "업데이트된 정책", 5000, false, null, null, 1000, true);
        Mockito.when(shipmentPolicyService.updateShipmentPolicy(eq(1L), any(ShipmentPolicyRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/shipment-policies/1")
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
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/shipment-policies/1/deactivate")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("배송 정책 활성화 테스트")
    void activateShipmentPolicy() throws Exception {
        // given
        Mockito.doNothing().when(shipmentPolicyService).activateShipmentPolicy(1L);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/shipment-policies/1/activate")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 여부에 따른 배송비 조회 테스트")
    void getShippingFee() throws Exception {
        // given
        Boolean isLogin = true;
        ShippingFeeResponseDto responseDto1 = new ShippingFeeResponseDto(1L, 10000, 5000);
        ShippingFeeResponseDto responseDto2 = new ShippingFeeResponseDto(2L, 20000, 3000);
        Mockito.when(shipmentPolicyService.getShippingFee(isLogin)).thenReturn(List.of(responseDto1, responseDto2));

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/shipment-policies/shipping-fee")
                        .param("isLogin", String.valueOf(isLogin))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shipmentPolicyId").value(1L))
                .andExpect(jsonPath("$[0].minOrderAmount").value(10000))
                .andExpect(jsonPath("$[0].shippingFee").value(5000))
                .andExpect(jsonPath("$[1].shipmentPolicyId").value(2L))
                .andExpect(jsonPath("$[1].minOrderAmount").value(20000))
                .andExpect(jsonPath("$[1].shippingFee").value(3000));
    }

}

