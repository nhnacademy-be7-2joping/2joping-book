package com.nhnacademy.bookstore.cart.dto;

import jakarta.validation.constraints.NotNull;

public record CartRequestDto (@NotNull(message = "필수") long bookId, @NotNull(message = "필수") String title, @NotNull(message = "필수") int sellingPrice, @NotNull(message = "필수") int quantity) {
}
