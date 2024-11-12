package com.nhnacademy.bookstore.bookset.publisher.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 출판사 Request DTO
 *
 * @author : 이유현
 * @date : 2024.10.23
 */

public record PublisherRequestDto (
    @NotBlank String name
) {}
