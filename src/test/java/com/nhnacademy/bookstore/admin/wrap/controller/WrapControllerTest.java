package com.nhnacademy.bookstore.admin.wrap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.admin.wrap.dto.request.*;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapCreateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapUpdateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.service.WrapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WrapController.class)
class WrapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WrapService wrapService;

    @Autowired
    private ObjectMapper objectMapper;

    private WrapRequestDto wrapRequestDto;
    private WrapUpdateRequestDto wrapUpdateRequestDto;
    private WrapCreateResponseDto wrapCreateResponseDto;
    private WrapUpdateResponseDto wrapUpdateResponseDto;

    @BeforeEach
    void setUp() {
        WrapDetailRequestDto wrapDetailRequestDto = new WrapDetailRequestDto("포장 정책", 1000, true);
        WrapImageUrlRequestDto imageUrlRequestDto = new WrapImageUrlRequestDto("http://example.com/image.jpg");
        wrapRequestDto = new WrapRequestDto(wrapDetailRequestDto, imageUrlRequestDto);

        WrapUpdateDetailRequestDto wrapUpdateDetailRequestDto = new WrapUpdateDetailRequestDto("수정 포장 정책", 2000, true);
        wrapUpdateRequestDto = new WrapUpdateRequestDto(wrapUpdateDetailRequestDto, imageUrlRequestDto, false);

        wrapCreateResponseDto = new WrapCreateResponseDto(1L, "포장 정책", 1000, true, "image1");
        wrapUpdateResponseDto = new WrapUpdateResponseDto(1L, "수정 포장 정책", 2000, true, "image2");
    }

    @Test
    void createWrap() throws Exception {
        when(wrapService.createWrap(any(WrapRequestDto.class))).thenReturn(wrapCreateResponseDto);

        mockMvc.perform(post("/api/v1/wraps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrapRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.wrapId").value(1L))
                .andExpect(jsonPath("$.name").value("포장 정책"))
                .andExpect(jsonPath("$.wrapPrice").value(1000))
                .andExpect(jsonPath("$.isActive").value(true));
    }


    @Test
    void getWrap() throws Exception {
        when(wrapService.getWrap(anyLong())).thenReturn(wrapUpdateResponseDto);

        mockMvc.perform(get("/api/v1/wraps/{wrap-id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wrapId").value(1L))
                .andExpect(jsonPath("$.name").value("수정 포장 정책"))
                .andExpect(jsonPath("$.wrapPrice").value(2000))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void findAllByIsActiveTrue() throws Exception {
        List<WrapUpdateResponseDto> wrapList = List.of(wrapUpdateResponseDto);

        when(wrapService.findAllByIsActiveTrue()).thenReturn(wrapList);

        mockMvc.perform(get("/api/v1/wraps")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].wrapId").value(1L))
                .andExpect(jsonPath("$[0].name").value("수정 포장 정책"))
                .andExpect(jsonPath("$[0].wrapPrice").value(2000))
                .andExpect(jsonPath("$[0].isActive").value(true));
    }

    @Test
    void updateWrap() throws Exception {
        when(wrapService.updateWrap(anyLong(), any(WrapUpdateRequestDto.class))).thenReturn(wrapUpdateResponseDto);

        mockMvc.perform(put("/api/v1/wraps/{wrap-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrapUpdateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wrapId").value(1L))
                .andExpect(jsonPath("$.name").value("수정 포장 정책"))
                .andExpect(jsonPath("$.wrapPrice").value(2000))
                .andExpect(jsonPath("$.isActive").value(true));
    }
}
