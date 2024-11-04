package com.nhnacademy.bookstore.like.dto;

/**
 * 좋아요 정보를 반환하는 DTO
 */
public record LikeResponseDto(
        Long likeId,
        Long bookId,
        Long memberId,
        Long likeCount //이거 꼭 필요할까?
) {}

//맞는지 모르겠다...