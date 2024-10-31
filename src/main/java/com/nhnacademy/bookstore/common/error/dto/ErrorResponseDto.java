package com.nhnacademy.bookstore.common.error.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private HttpStatus status;
    private String errorCode;
    private String errorMessage;
    private String redirectType; // "REDIRECT" or "FORWARD" or "NONE"
    private String url;


    /**
     * 기본 리다이렉트 없이 오류 정보를 포함하는 생성자.
     * @param status 오류 상태 코드
     * @param errorCode 오류 코드
     * @param errorMessage 오류 메시지
     */
    public ErrorResponseDto(HttpStatus status, String errorCode, String errorMessage) {
        this(status, errorCode, errorMessage, "NONE", null);
    }
}
