package com.nhnacademy.bookstore.common.error.exception.coupon;


import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;

/**
 * DuplicateCouponNameException
 * 이 예외 클래스는 애플리케이션에서 중복된 쿠폰 이름이 감지되었을 때 발생하는 HTTP 400 오류를 나타냅니다.
 * 이 예외는 쿠폰 이름의 고유성을 보장하기 위한 검사 중에 발생하며, 리디렉션 정보와 추가 데이터를 전달할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
public class DuplicateCouponNameException extends BadRequestException {

    public DuplicateCouponNameException(String message, RedirectType redirectType, String url) {
        super(message, redirectType, url);
    }
}
