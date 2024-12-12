package com.nhnacademy.bookstore.bookset.tag.dto;

/**
 * 태그 정보를 반환하는 DTO
 */
public record TagResponseDto(
        Long tagId,
        String name
) {
}
