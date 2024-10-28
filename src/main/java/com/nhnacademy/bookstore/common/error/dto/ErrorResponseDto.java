package com.nhnacademy.bookstore.common.error.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private int status;
    private String errorCode;
    private String errorMessage;

    public ErrorResponseDto(HttpStatus status, String errorCode, String errorMessage) {
        this.status = status.value();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
