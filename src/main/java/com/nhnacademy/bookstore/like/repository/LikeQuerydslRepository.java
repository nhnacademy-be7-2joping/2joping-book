package com.nhnacademy.bookstore.like.repository;

import com.nhnacademy.bookstore.like.dto.response.MemberLikeResponseDto;

import java.util.List;

public interface LikeQuerydslRepository {
    List<MemberLikeResponseDto> findLikesByMember(Long memberId);
}
