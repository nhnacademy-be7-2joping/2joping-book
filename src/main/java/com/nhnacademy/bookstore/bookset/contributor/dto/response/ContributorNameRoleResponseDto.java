package com.nhnacademy.bookstore.bookset.contributor.dto.response;

/**
 * 도서 기여자 이름 및 역할 response dto
 *
 * @author : 이초은
 * @date : 2024-11-22
 */
public record ContributorNameRoleResponseDto (
    String contributorName,
    String contributorRole
) {}