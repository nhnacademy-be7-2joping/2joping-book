package com.nhnacademy.bookstore.user.customer.dto.request;

public record CustomerRegisterRequest(
        String name,
        String phone,
        String email
) {
}
