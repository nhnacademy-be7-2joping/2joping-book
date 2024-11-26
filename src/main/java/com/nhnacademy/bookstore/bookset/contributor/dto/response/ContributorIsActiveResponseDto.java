package com.nhnacademy.bookstore.bookset.contributor.dto.response;

public record ContributorIsActiveResponseDto(
        Long contributorId,
        Long contributorRoleId,
        String name,
        Boolean isActive
) {}

