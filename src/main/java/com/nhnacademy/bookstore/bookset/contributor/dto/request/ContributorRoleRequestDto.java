package com.nhnacademy.bookstore.bookset.contributor.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 도서 기여자 역할 create request dto
 *
 * @author : 양준하
 * @date : 2024-10-24
 */
public record ContributorRoleRequestDto(
        @NotBlank(message = "기여자 역할 이름 입력: ") String name
) {}
