package com.nhnacademy.bookstore.bookset.publisher.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.nhnacademy.bookstore.bookset.publisher.dto.request.PublisherRequestDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherCreateResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.service.PublisherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(PublisherController.class)
@TestPropertySource(properties = "keymanager.url=http://localhost:8084")
public class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;


    @Test
    @DisplayName("출판사 등록")
    public void testRegisterPublisher_Success() throws Exception {
        //given
        PublisherCreateResponseDto createResponseDto = new PublisherCreateResponseDto(1L,"출판사 1");

        when(publisherService.registerPublisher(any(PublisherRequestDto.class))).thenReturn(createResponseDto);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/bookstore/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"출판사 1\"}"));
        //then
        resultActions.andExpect(status().isCreated  ())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("출판사 1"));
    }

    @Test
    @DisplayName("전체 출판사 조회")
    public void testGetAllPublishers() throws Exception {
        // given
        List<PublisherResponseDto> responseList = Arrays.asList(
                new PublisherResponseDto(1L, "출판사1"),
                new PublisherResponseDto(2L, "출판사2")
        );
        Page<PublisherResponseDto> pageResponse = new PageImpl<>(responseList);
        when(publisherService.getAllPublishers(any(PageRequest.class))).thenReturn(pageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/bookstore/publishers")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("출판사1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("출판사2"));
    }

    @Test
    @DisplayName("특정 출판사 조회")
    public void testGetPublisher_Success() throws Exception {
        // given
        PublisherResponseDto responseDto = new PublisherResponseDto(1L, "출판사 이름");

        when(publisherService.getPublisherById(1L)).thenReturn(responseDto);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/bookstore/publisher/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("출판사 이름"));
    }

    @Test
    @DisplayName("출판사 삭제")
    public void testDeletePublisher_Success() throws Exception {
        // given
        doNothing().when(publisherService).deletePublisher(1L);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/bookstore/publisher/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("출판사 수정")
    public void testUpdatePublisher_Success() throws Exception {
        // given
        PublisherRequestDto requestDto = new PublisherRequestDto("업데이트된 출판사 이름");
        PublisherResponseDto responseDto = new PublisherResponseDto(1L, "업데이트된 출판사 이름");

        when(publisherService.updatePublisher(eq(1L), any(PublisherRequestDto.class))).thenReturn(responseDto);

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/bookstore/publisher/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"업데이트된 출판사 이름\"}"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("업데이트된 출판사 이름"));
    }
}
