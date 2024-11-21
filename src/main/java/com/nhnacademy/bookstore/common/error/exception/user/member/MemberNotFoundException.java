package com.nhnacademy.bookstore.common.error.exception.user.member;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

import java.io.Serializable;


/**
 * MemberNotFoundException
 * 회원 정보를 찾지 못할 때 발생하는 예외 클래스입니다.
 * 기본적으로 HTTP 404 Not Found 상태를 나타내며, 추가적으로 리다이렉션 타입, URL, 데이터를 포함할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
public class MemberNotFoundException extends NotFoundException {

    /**
     * MemberNotFoundException 생성자.
     *
     * @param message      예외 메시지
     * @param redirectType 리다이렉션 타입 (예: REDIRECT, FORWARD 등)
     * @param url          리다이렉션 대상 URL
     */
    public MemberNotFoundException(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }

    /**
     * MemberNotFoundException 생성자.
     *
     * @param message      예외 메시지
     * @param redirectType 리다이렉션 타입 (예: REDIRECT, FORWARD 등)
     * @param url          리다이렉션 대상 URL
     * @param data         추가 데이터 (필요한 경우)
     */
    public MemberNotFoundException(String message, RedirectType redirectType, String url, Serializable data) {
        super(message, redirectType, url, data);
    }
}
