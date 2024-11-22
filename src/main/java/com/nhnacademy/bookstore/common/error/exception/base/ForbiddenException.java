package com.nhnacademy.bookstore.common.error.exception.base;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import lombok.Getter;

import java.io.Serializable;

/**
 * ForbiddenException
 * 이 예외는 애플리케이션 내에서 접근 금지 오류(HTTP 403)를 나타냅니다.
 * 추가 필드를 통해 리디렉션 정보와 추가 데이터를 처리할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@Getter
public class ForbiddenException extends RuntimeException{

    private final RedirectType redirectType;
    private final String url;
    private final Serializable data;

    public ForbiddenException(String message) {
        super(message);
        this.redirectType = null;
        this.url = null;
        this.data = null;
    }
    public ForbiddenException(String message, RedirectType redirectType, String url) {
        super(message);
        this.redirectType = redirectType;
        this.url = url;
        this.data = null;

    }
    public ForbiddenException(String message, RedirectType redirectType, String url, Serializable data) {
        super(message);
        this.redirectType = redirectType;
        this.url = url;
        this.data = data;
    }
}