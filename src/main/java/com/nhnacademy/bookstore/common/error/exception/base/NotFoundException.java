package com.nhnacademy.bookstore.common.error.exception.base;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import lombok.Getter;

import java.io.Serializable;

/**
 * NotFoundException
 * 이 예외 클래스는 애플리케이션에서 리소스를 찾을 수 없을 때 발생하는 HTTP 404 오류를 나타냅니다.
 * 추가 필드를 통해 리디렉션 정보와 추가 데이터를 전달할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@Getter
public class NotFoundException extends RuntimeException {

    private final RedirectType redirectType;
    private final String url;
    private final Serializable data;

    public NotFoundException(String message) {
        super(message);
        this.redirectType = null;
        this.url = null;
        this.data = null;
    }
    public NotFoundException(String message, RedirectType redirectType, String url) {
        super(message);
        this.redirectType = redirectType;
        this.url = url;
        this.data = null;

    }
    public NotFoundException(String message, RedirectType redirectType, String url, Serializable data) {
        super(message);
        this.redirectType = redirectType;
        this.url = url;
        this.data = data;
    }
}