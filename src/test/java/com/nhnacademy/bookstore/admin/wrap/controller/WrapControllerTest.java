package com.nhnacademy.bookstore.admin.wrap.controller;

import com.nhnacademy.bookstore.admin.wrap.controller.WrapController;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WrapController.class)
class WrapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WrapService wrapService;

    private WrapRequestDto wrapRequestDto;
    private WrapResponseDto wrapResponseDto;
    private WrapResponseDto wrapResponseDto2;

    @BeforeEach
    void setUp() {
        wrapRequestDto = new WrapRequestDto("포장 정책", 1000, true);
        wrapResponseDto = new WrapResponseDto(1L, "포장 정책", 1000, true);
        wrapResponseDto2 = new WrapResponseDto(2L, "포장 정책 2", 2000, false);


    }

    @Test
    void createWrap() throws Exception {
        doNothing().when(wrapService).createWrap(any(WrapRequestDto.class));

        mockMvc.perform(post("/api/v1/wraps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"포장 정책\",\"wrapPrice\":1000,\"isActive\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getWrap() throws Exception {
        when(wrapService.getWrap(anyLong())).thenReturn(wrapResponseDto);

        mockMvc.perform(get("/api/v1/wraps/{wrap-id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wrapId").value(1L))
                .andExpect(jsonPath("$.name").value("포장 정책"))
                .andExpect(jsonPath("$.wrapPrice").value(1000))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void findAllByIsActiveTrue() throws Exception {
        List<WrapResponseDto> wrapList = List.of(wrapResponseDto);

        when(wrapService.findAllByIsActiveTrue()).thenReturn(wrapList);

        mockMvc.perform(get("/api/v1/wraps")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].wrapId").value(1L))
                .andExpect(jsonPath("$[0].name").value("포장 정책"))
                .andExpect(jsonPath("$[0].wrapPrice").value(1000))
                .andExpect(jsonPath("$[0].isActive").value(true));
    }

    @Test
    void updateWrap() throws Exception {
        WrapResponseDto updatedWrapResponseDto = new WrapResponseDto(1L, "수정 포장 정책", 2000, true);

        when(wrapService.updateWrap(anyLong(), any(WrapRequestDto.class))).thenReturn(updatedWrapResponseDto);

        mockMvc.perform(put("/api/v1/wraps/{wrap-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"수정 포장 정책\",\"wrapPrice\":2000,\"isActive\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wrapId").value(1L))
                .andExpect(jsonPath("$.name").value("수정 포장 정책"))
                .andExpect(jsonPath("$.wrapPrice").value(2000))
                .andExpect(jsonPath("$.isActive").value(true));
    }
    }


//    @Test
//    void deleteWrap() throws Exception {
//        doNothing().when(wrapService).findAllByIsActiveTrue(anyLong());
//        List<WrapResponseDto> wrapList = List.of(
//                new WrapResponseDto(1L, "포장 상품1", 1000, true),
//                new WrapResponseDto(2L, "포장 상품2", 2000, false)
//        );
//        when(wrapService.findAllByIsActiveTrue()).thenReturn(wrapList);
//
//        mockMvc.perform(delete("/api/v1/wraps/{wrap-id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(wrapList.size()))
//                .andExpect(jsonPath("$[0].name").value("포장 상품1"))
//                .andExpect(jsonPath("$[1].name").value("포장 상품2"));
//    }
//
//}
