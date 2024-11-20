package com.nhnacademy.bookstore.cart.dto;

public record CartResponseDto (long bookId, String title,long sellingPrice, int quantity) {

}
