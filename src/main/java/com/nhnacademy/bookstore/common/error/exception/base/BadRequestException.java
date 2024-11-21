package com.nhnacademy.bookstore.common.error.exception.base;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import lombok.Getter;

import java.io.Serializable;

/**
 * BadRequestException
 * 이 클래스는 잘못된 요청에 대한 예외를 나타내며, 요청이 유효하지 않은 경우에 발생합니다.
 * 특정 페이지로의 리다이렉트 및 추가 데이터를 포함하여 예외 처리에 대한 상세 정보를 제공합니다.
 * 예외 발생 시, 예외 메시지와 함께 리다이렉트 타입과 URL, 추가 데이터를 설정할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@Getter
public class BadRequestException extends RuntimeException {

    private final RedirectType redirectType;
    private final String url;
    private final Serializable data;

    /**
     * 기본 생성자.
     * 예외 메시지만 설정하며, 리다이렉트 타입, URL, 데이터는 설정하지 않습니다.
     *
     * @param message 예외 메시지
     */
    public BadRequestException(String message) {
        super(message);
        this.redirectType = null;
        this.url = null;
        this.data = null;
    }

    /**
     * 리다이렉트 타입과 URL을 포함한 생성자.
     *
     * @param message 예외 메시지
     * @param redirectType 리다이렉트 타입
     * @param url 리다이렉트될 URL
     */
    public BadRequestException(String message, RedirectType redirectType, String url) {
        super(message);
        this.redirectType = redirectType;
        this.url = url;
        this.data = null;
    }

    /**
     * 리다이렉트 타입, URL, 추가 데이터를 포함한 생성자.
     *
     * @param message 예외 메시지
     * @param redirectType 리다이렉트 타입
     * @param url 리다이렉트될 URL
     * @param data 추가 데이터
     */
    public BadRequestException(String message, RedirectType redirectType, String url, Serializable data) {
        super(message);
        this.redirectType = redirectType;
        this.url = url;
        this.data = data;
    }

}
