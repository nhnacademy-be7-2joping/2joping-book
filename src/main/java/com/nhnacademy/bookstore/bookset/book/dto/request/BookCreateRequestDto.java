package com.nhnacademy.bookstore.bookset.book.dto.request;

/**
 * 도서 생성 Request DTO
 *
 * @author : 이초은
 * @date : 2024-10-31
 */

public record BookCreateRequestDto(BookCreateHtmlRequestDto bookCreateHtmlRequestDto, ImageUrlRequestDto imageUrlRequestDto) {}