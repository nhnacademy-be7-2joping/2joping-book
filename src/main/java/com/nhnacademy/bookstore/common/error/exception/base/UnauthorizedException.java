package com.nhnacademy.bookstore.common.error.exception.base;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import lombok.Getter;

import java.io.Serializable;

/**
 * UnauthorizedException 클래스
 * 이 예외는 애플리케이션에서 인증되지 않은 액세스(HTTP 401)를 나타냅니다.
 * 이 클래스는 리다이렉션 처리와 추가 데이터 전달을 위한 필드를 포함하고 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@Getter
public class UnauthorizedException  extends RuntimeException{

    private final RedirectType redirectType;
    private final String url;
    private final Serializable data;

    public UnauthorizedException(String message) {
        super(message);
        this.redirectType = null;
        this.url = null;
        this.data = null;
    }

    public UnauthorizedException(String message, RedirectType redirectType, String url) {
        super(message);
        this.redirectType = redirectType;
        this.url = url;
        this.data = null;
    }

    public UnauthorizedException(String message, RedirectType redirectType, String url, Serializable data) {
        super(message);
        this.redirectType = redirectType;
        this.url = url;
        this.data = data;
    }
}
