package com.nhnacademy.bookstore.admin.wrappolicy.dto;

/**
 * 포장 정책 정보를 반환하는 DTO
 */
public record WrapPolicyResponseDto (
    Long wrapId,
    String name,
    int wrapPrice,
    boolean isActive
) {}
