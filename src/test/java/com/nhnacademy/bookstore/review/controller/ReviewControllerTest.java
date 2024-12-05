package com.nhnacademy.bookstore.review.controller;

import com.nhnacademy.bookstore.review.dto.request.*;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewTotalResponseDto;
import com.nhnacademy.bookstore.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private ReviewCreateRequestDto createRequestDto;
    private ReviewModifyRequestDto modifyRequestDto;
    private ReviewCreateResponseDto createResponseDto;
    private ReviewModifyResponseDto modifyResponseDto;
    private ReviewResponseDto reviewResponseDto;
    private ReviewTotalResponseDto reviewTotalResponseDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        createRequestDto = new ReviewCreateRequestDto(
                new ReviewDetailRequestDto(1L, 1L, 5,"제목", "내용"),
                new ReviewImageUrlRequestDto("test-image.jpg")
        );

        modifyRequestDto = new ReviewModifyRequestDto(
                new ReviewModifyDetailRequestDto(1L, 4, "수정된 제목", "수정된 내용"),
                new ReviewImageUrlRequestDto("updated-image.jpg"),
                false
        );

        createResponseDto = new ReviewCreateResponseDto(1L, 2,"생성된 제목","생성된 내용","생성된 리뷰 이미지",Timestamp.valueOf(LocalDateTime.now()));
        modifyResponseDto = new ReviewModifyResponseDto(1L, 5,"수정된 제목","수정된 내용","수정된 리뷰 이미지",Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now()));

        reviewResponseDto = new ReviewResponseDto(
                1L, 1L, 1L, 1L, 5, "제목", "내용", "test-image.jpg",
                Timestamp.valueOf(LocalDateTime.now()), null
        );

        reviewTotalResponseDto = new ReviewTotalResponseDto(
                1L, 1L, 1L, 1L, 5,"책 제목", "제목", "내용", "test-image.jpg",
                Timestamp.valueOf(LocalDateTime.now()), null
        );

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("리뷰 등록 테스트")
    void registerReview() throws Exception {
        when(reviewService.registerReview(any(ReviewCreateRequestDto.class))).thenReturn(createResponseDto);

        mockMvc.perform(post("/api/v1/bookstore/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewDetailRequestDto\":{\"customerId\":1,\"orderDetailId\":1,\"ratingValue\":5,\"title\":\"제목\",\"text\":\"내용\"},\"reviewImageUrlRequestDto\":{\"reviewImage\":\"test-image.jpg\"}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewId").value(1));
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    void modifyReview() throws Exception {
        when(reviewService.modifyReview(any(ReviewModifyRequestDto.class))).thenReturn(modifyResponseDto);

        mockMvc.perform(put("/api/v1/bookstore/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewModifyDetailRequestDto\":{\"reviewId\":1,\"ratingValue\":4,\"title\":\"수정된 제목\",\"text\":\"수정된 내용\"},\"reviewImageUrlRequestDto\":{\"reviewImage\":\"updated-image.jpg\"},\"deleteImage\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(1));
    }

    @Test
    @DisplayName("리뷰 단일 조회 테스트")
    void getReview() throws Exception {
        when(reviewService.getReviews(any(ReviewRequestDto.class))).thenReturn(Optional.of(reviewResponseDto));

        mockMvc.perform(get("/api/v1/bookstore/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(1));
    }

    @Test
    @DisplayName("도서별 리뷰 조회 테스트")
    void getReviewsByBookId() throws Exception {
        Page<ReviewResponseDto> responseDtoPage = new PageImpl<>(Collections.singletonList(reviewResponseDto), pageable, 1);
        when(reviewService.getReviewsByBookId(any(Pageable.class), any(Long.class))).thenReturn(responseDtoPage);

        mockMvc.perform(get("/api/v1/bookstore/reviews/book/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId").value(1));
    }

    @Test
    @DisplayName("회원별 리뷰 조회 테스트")
    void getReviewsByCustomerId() throws Exception {
        Page<ReviewTotalResponseDto> responseDtoPage = new PageImpl<>(Collections.singletonList(reviewTotalResponseDto), pageable, 1);
        when(reviewService.getReviewsByCustomerId(any(Pageable.class), any(Long.class))).thenReturn(responseDtoPage);

        mockMvc.perform(get("/api/v1/bookstore/reviews/customer")
                        .header("X-Customer-Id", "12345")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId").value(1))
                .andExpect(jsonPath("$.content[0].bookName").value("책 제목"));
    }

}

