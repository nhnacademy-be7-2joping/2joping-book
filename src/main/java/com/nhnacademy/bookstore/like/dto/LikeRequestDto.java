package com.nhnacademy.bookstore.like.dto;

import jakarta.validation.constraints.Positive;

/**
 * 좋아요 생성/삭제 요청을 위한 DTO
 */
    public record LikeRequestDto (

            @Positive
            Long bookId
    ) {}


