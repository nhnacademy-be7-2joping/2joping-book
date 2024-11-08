package com.nhnacademy.bookstore.bookset.publisher.dto.response;


import jakarta.validation.constraints.Positive;

/**
 * 출판사 CreateResponseDTO
 *
 * @author : 이유현
 * @date : 2024.10.23
 */

public record PublisherCreateResponseDto (
    @Positive Long id,
     String name
) {}