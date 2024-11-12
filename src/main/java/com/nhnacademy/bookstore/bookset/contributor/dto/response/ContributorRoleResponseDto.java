package com.nhnacademy.bookstore.bookset.contributor.dto.response;

/**
 * 도서 기여자 역할 create response dto
 *
 * @author : 양준하
 * @date : 2024-10-24
 */
public record ContributorRoleResponseDto(
        Long contributorRoleId,
        String name
) {}
