package com.nhnacademy.bookstore.common.error.exception.user.member.status;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;


/**
 * MemberStatusNotFoundException
 * 회원 상태를 찾을 수 없을 때 발생하는 예외 클래스입니다.
 * 회원 상태가 유효하지 않거나 존재하지 않을 경우 이 예외가 발생합니다.
 * 기본적으로 HTTP 404 Not Found 상태를 나타내며, 추가적으로 리다이렉션 타입, URL, 데이터를 포함할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
public class MemberStatusNotFoundException extends NotFoundException {

    /**
     * MemberStatusNotFoundException 생성자.
     *
     * @param message       예외 메시지
     * @param redirectType  리다이렉션 타입 (예: REDIRECT, FORWARD 등)
     * @param url           리다이렉션 대상 URL
     * @since 1.0
     * @see NotFoundException
     */
    public MemberStatusNotFoundException(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }
}
