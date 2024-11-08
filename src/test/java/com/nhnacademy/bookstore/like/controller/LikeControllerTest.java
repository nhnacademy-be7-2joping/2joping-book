package com.nhnacademy.bookstore.like.controller;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.like.dto.LikeRequestDto;
import com.nhnacademy.bookstore.like.dto.LikeResponseDto;
import com.nhnacademy.bookstore.like.service.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;


    @Test
    void setBookLike_success() throws Exception {
        LikeRequestDto requestDto = new LikeRequestDto(1L, 1L);
        LikeResponseDto responseDto = new LikeResponseDto(1L, 1L, 1L, 10L);

        when(likeService.setBookLike(any(LikeRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\": 1, \"bookId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeId").value(1L))
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.likeCount").value(10L));
    }

    @Test
    void getLikeCount_success() throws Exception {
        when(likeService.getLikeCount(anyLong())).thenReturn(10L);

        mockMvc.perform(get("/api/v1/likes/count/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void getBooksLikedByMember_success() throws Exception {
        Book book = new Book(1L, null, "Sample Book", "A description", null, "1234567890123", 1000, 900, true, true, 10, 100, 50);
        List<Book> books = Collections.singletonList(book);

        when(likeService.getBooksLikedByCustomer(anyLong())).thenReturn(books);

        mockMvc.perform(get("/api/v1/likes/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Sample Book"))
                .andExpect(jsonPath("$[0].isbn").value("1234567890123"));
    }
}
