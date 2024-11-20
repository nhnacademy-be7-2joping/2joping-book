package com.nhnacademy.bookstore.common.error.exception.user.member.status;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
/**
 * 회원 정보 수정 시 변경할 내용이 없는 경우 발생하는 예외 클래스입니다.
 * 이 예외는 클라이언트가 회원 정보를 수정 요청했으나, 실제로 업데이트할 내용이 없는 경우에 발생합니다.
 *
 * @author Luha
 * @since 1.0
 */
public class MemberNothingToUpdateException extends NotFoundException {
    public MemberNothingToUpdateException(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }
}
