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
    void createWrapPolicy() throws Exception {
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
    void getAllWraps() throws Exception {
       // WrapResponseDto wrapResponseDto2 = new WrapResponseDto(2L, "포장 정책 2", 2000, false);

        List<WrapResponseDto> wrapList = List.of(wrapResponseDto, wrapResponseDto2);

        when(wrapService.getAllWraps()).thenReturn(wrapList);

        mockMvc.perform(get("/api/v1/wraps/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].wrapId").value(1L))
                .andExpect(jsonPath("$[0].name").value("포장 정책"))
                .andExpect(jsonPath("$[0].wrapPrice").value(1000))
                .andExpect(jsonPath("$[0].isActive").value(true))
                .andExpect(jsonPath("$[1].wrapId").value(2L))
                .andExpect(jsonPath("$[1].name").value("포장 정책 2"))
                .andExpect(jsonPath("$[1].wrapPrice").value(2000))
                .andExpect(jsonPath("$[1].isActive").value(false));
    }
    @Test
    void updateWrap() throws Exception {
        // 수정된 wrapResponseDto를 설정합니다.
        WrapResponseDto updatedWrapResponseDto = new WrapResponseDto(1L, "수정 포장 정책", 2000, true);

        // 서비스 메서드가 수정된 객체를 반환하도록 Mock 설정
        when(wrapService.updateWrap(anyLong(), any(WrapRequestDto.class))).thenReturn(updatedWrapResponseDto);

        // 수정 요청을 보내고, 결과를 검증
        mockMvc.perform(put("/api/v1/wraps/{wrap-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"수정 포장 정책\",\"wrapPrice\":2000,\"isActive\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wrapId").value(1L))
                .andExpect(jsonPath("$.name").value("수정 포장 정책"))
                .andExpect(jsonPath("$.wrapPrice").value(2000))
                .andExpect(jsonPath("$.isActive").value(true));
    }


    @Test
    void deleteWrap() throws Exception {
        doNothing().when(wrapService).deleteWrap(anyLong());

        mockMvc.perform(delete("/api/v1/wraps/{wrap-id}", 1L))
                .andExpect(status().isNoContent());
    }
}
