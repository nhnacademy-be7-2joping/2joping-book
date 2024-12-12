package com.nhnacademy.bookstore.bookset.book.dto.response;

import jakarta.validation.constraints.Positive;

/**
 * 도서 기여자 Response DTO
 *
 * @author : 이유현
 * @date : 2024-11-03
 */


public record BookContributorResponseDto(
        @Positive Long contributorId,
        String contributorName,
        Long roleId,
        String roleName
) {
}
