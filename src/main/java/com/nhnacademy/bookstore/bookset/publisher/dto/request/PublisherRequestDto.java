package com.nhnacademy.bookstore.bookset.publisher.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 출판사 Request DTO
 *
 * @author : 이유현
 * @date : 2024.10.23
 */

@Getter
@NoArgsConstructor
public class PublisherRequestDto {
    @NotBlank // notnull empty blank
    private String name;


}
