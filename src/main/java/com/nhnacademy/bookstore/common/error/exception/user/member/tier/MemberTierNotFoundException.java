package com.nhnacademy.bookstore.common.error.exception.user.member.tier;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

import java.io.Serializable;

/**
 * MemberTierNotFoundException
 * 회원 등급을 찾을 수 없을 때 발생하는 예외 클래스입니다.
 * 이 예외는 회원이 속한 등급이 존재하지 않거나 조회할 수 없는 경우에 사용됩니다.
 * 기본적으로 HTTP 404 Not Found 상태를 나타내며, 추가적으로 리다이렉션 타입, URL, 데이터를 포함할 수 있습니다.
 *
 * @since 1.0
 * @see NotFoundException
 * @author Luha
 */
public class MemberTierNotFoundException extends NotFoundException {

    /**
     * MemberTierNotFoundException 생성자
     *
     * @param message       예외 메시지
     * @param redirectType  리다이렉션 타입
     * @param url           리다이렉션 대상 URL
     * @param data          추가 데이터
     */
    public MemberTierNotFoundException(String message, RedirectType redirectType, String url, Serializable data) {
        super(message, redirectType, url, data);
    }
}
