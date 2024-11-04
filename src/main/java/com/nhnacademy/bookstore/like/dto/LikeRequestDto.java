package com.nhnacademy.bookstore.like.dto;

import jakarta.validation.constraints.Positive;

/**
 * 좋아요 생성/삭제 요청을 위한 DTO
 */
    public record LikeRequestDto (

            // 여기서 positive는 검사 안해도 되는지?
            // 작업을 요청할때 필요한 최소한의 정보만

            Long memberId,

            Long bookId
    ) {}


