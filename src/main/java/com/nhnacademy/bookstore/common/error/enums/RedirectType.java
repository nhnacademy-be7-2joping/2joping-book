package com.nhnacademy.bookstore.common.error.enums;

/**
 * RedirectType
 * 리다이렉션 유형을 정의하는 열거형입니다. 다음 세 가지 옵션을 제공합니다:
 * - REDIRECT: 클라이언트를 다른 URL로 리다이렉트합니다.
 * - FORWARD: 요청을 다른 내부 리소스로 포워드합니다.
 * - NONE: 리다이렉션을 수행하지 않습니다.
 *
 * @author Luha
 * @since 1.0
 */
public enum RedirectType {
    REDIRECT, FORWARD, NONE
}
