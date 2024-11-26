package com.nhnacademy.bookstore.bookset.contributor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 도서 기여자 create request dto
 *
 * @author : 양준하
 * @date : 2024-10-24
 */
public record ContributorRequestDto(
        @NotBlank(message = "기여자 이름 입력: ") String name,
        @NotNull Long contributorRoleId
) {}
