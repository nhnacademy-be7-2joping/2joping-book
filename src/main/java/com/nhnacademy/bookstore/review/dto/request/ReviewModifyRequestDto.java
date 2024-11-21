package com.nhnacademy.bookstore.review.dto.request;


public record ReviewModifyRequestDto (
        ReviewModifyDetailRequestDto reviewModifyDetailRequestDto,
        ReviewImageUrlRequestDto reviewImageUrlRequestDto,
        boolean deleteImage
)
{}
