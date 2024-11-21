package com.nhnacademy.bookstore.review.dto.request;


/**
 * 리뷰 생성할 때 Request DTO
 *
 * @author : 이유현
 * @date : 2024-11-12
 */

public record ReviewCreateRequestDto (
    ReviewDetailRequestDto reviewDetailRequestDto,
    ReviewImageUrlRequestDto reviewImageUrlRequestDto
)
{}
