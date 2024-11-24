package com.nhnacademy.bookstore.common.error.dto;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;

/**
 * ErrorResponseDto
 * 이 클래스는 API 응답에서 발생하는 오류에 대한 정보를 포함하는 DTO입니다.
 * 상태 코드, 오류 코드 및 메시지, 리디렉션 타입, URL, 그리고 관련 데이터를 담고 있습니다.
 *
 * @param <T> 오류 발생 시 추가적으로 반환할 데이터 타입
 * @param status HTTP 상태 코드
 * @param errorCode 사용자 정의 오류 코드
 * @param errorMessage 오류 메시지
 * @param redirectType 리디렉션 타입 ("REDIRECT", "FORWARD", "NONE")
 * @param url 리디렉션 URL
 * @param data 추가 데이터
 *
 * @author Luha
 * @since 1.0
 */
public record ErrorResponseDto<T>(
        int  status,
        String errorCode,
        String errorMessage,
        RedirectType redirectType, // "REDIRECT" or "FORWARD" or "NONE"
        String url,
        T data
) {

}