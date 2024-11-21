package com.nhnacademy.bookstore.common.error.exception.base;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import lombok.Getter;

import java.io.Serializable;

/**
 * ConflictException
 * 이 예외는 애플리케이션에서 발생하는 충돌 오류(HTTP 409)를 나타냅니다.
 * 리다이렉션 처리 및 추가 데이터를 전달하기 위한 필드를 제공합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Getter
public class ConflictException extends RuntimeException{

    private final RedirectType redirectType;
    private final String url;
    private final Serializable data;

    /**
     * 기본 생성자.
     * 예외 메시지만 설정하며, 리다이렉트 타입, URL, 데이터는 설정하지 않습니다.
     *
     * @param message 예외 메시지
     */
    public ConflictException(String message) {
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
    public ConflictException(String message, RedirectType redirectType, String url) {
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
    public ConflictException(String message, RedirectType redirectType, String url, Serializable data) {
        super(message);
        this.redirectType = redirectType;
        this.url = url;
        this.data = data;
    }
}