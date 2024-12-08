package com.nhnacademy.bookstore.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.request.UpdatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AdminPointController에 대한 테스트 클래스.
 * author: 박채연
 * date: 2024-11-20
 */
@WebMvcTest(AdminPointController.class)
class PointTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointTypeServiceImpl pointTypeServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 포인트 타입 생성 테스트.
     * 포인트 타입 생성 요청이 성공적으로 처리되고, 응답 데이터가 올바른지 확인.
     */
    @Test
    @DisplayName("포인트 타입 생성 테스트")
    void createPointType() throws Exception {
        CreatePointTypeRequestDto requestDto = new CreatePointTypeRequestDto(PointTypeEnum.PERCENT, 10, "구매 포인트", true);

        Mockito.when(pointTypeServiceImpl.createPointType(any(CreatePointTypeRequestDto.class))).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/pointtypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/admin/pointtypes/1"));
    }

    /**
     * 포인트 타입 수정 테스트.
     * 포인트 타입 수정 요청이 성공적으로 처리되고, 수정된 데이터가 응답으로 반환되는지 확인.
     */
    @Test
    @DisplayName("포인트 타입 수정 테스트")
    void updatePointType() throws Exception {
        UpdatePointTypeRequestDto requestDto = new UpdatePointTypeRequestDto(PointTypeEnum.ACTUAL, 10, "구매 포인트", true);
        UpdatePointTypeResponseDto responseDto = new UpdatePointTypeResponseDto(1L, PointTypeEnum.PERCENT, 10, "리뷰 포인트", true);

        Mockito.when(pointTypeServiceImpl.updatePointType(anyLong(), any(UpdatePointTypeRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/admin/pointtypes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pointTypeId").value(1L))
                .andExpect(jsonPath("$.type").value(PointTypeEnum.PERCENT.toString()))
                .andExpect(jsonPath("$.accVal").value(10))
                .andExpect(jsonPath("$.name").value("리뷰 포인트"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    /**
     * 활성화된 포인트 타입 목록 조회 테스트.
     * 활성화된 포인트 타입만 반환되는지 확인.
     */
    @Test
    @DisplayName("활성화된 포인트 타입 목록 조회 테스트")
    void getAllActivePointTypes() throws Exception {
        GetPointTypeResponse activeResponseDto1 = new GetPointTypeResponse(1L, PointTypeEnum.ACTUAL, 10, "리뷰 포인트1", true);
        GetPointTypeResponse activeResponseDto2 = new GetPointTypeResponse(2L, PointTypeEnum.PERCENT, 15, "리뷰 포인트2", true);

        Mockito.when(pointTypeServiceImpl.getAllActivePointTypes())
                .thenReturn(Arrays.asList(activeResponseDto1, activeResponseDto2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/pointtypes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pointTypeId").value(1L))
                .andExpect(jsonPath("$[0].type").value(PointTypeEnum.ACTUAL.toString()))
                .andExpect(jsonPath("$[0].accVal").value(10))
                .andExpect(jsonPath("$[0].name").value("리뷰 포인트1"))
                .andExpect(jsonPath("$[0].isActive").value(true))
                .andExpect(jsonPath("$[1].pointTypeId").value(2L))
                .andExpect(jsonPath("$[1].type").value(PointTypeEnum.PERCENT.toString()))
                .andExpect(jsonPath("$[1].accVal").value(15))
                .andExpect(jsonPath("$[1].name").value("리뷰 포인트2"))
                .andExpect(jsonPath("$[1].isActive").value(true));
    }

    /**
     * 특정 포인트 타입 조회 테스트.
     * ID로 포인트 타입을 조회하고 응답 데이터가 올바른지 확인.
     */
    @Test
    @DisplayName("특정 포인트 타입 조회 테스트")
    void getPointTypeById() throws Exception {
        ReadPointTypeResponseDto responseDto = new ReadPointTypeResponseDto(1L, PointTypeEnum.ACTUAL, 10, "리뷰 포인트", true);

        Mockito.when(pointTypeServiceImpl.getPointTypeById(anyLong())).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/pointtypes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pointTypeId").value(1L))
                .andExpect(jsonPath("$.type").value(PointTypeEnum.ACTUAL.toString()))
                .andExpect(jsonPath("$.accVal").value(10))
                .andExpect(jsonPath("$.name").value("리뷰 포인트"))
                .andExpect(jsonPath("$.isActive").value(true));
    }
}
